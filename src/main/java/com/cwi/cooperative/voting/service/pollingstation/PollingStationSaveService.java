package com.cwi.cooperative.voting.service.pollingstation;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PollingStationSaveService implements IValideRules {
	
	@Autowired
	private PollingStationRepository pollingStationRepository;
	
	public void execute(PollingStation pollingStation) {
		validateRules(pollingStation);		
		
		/* Begin Rule (1 minute by default)
		 * 
		 * Open a voting session on an agenda (the voting session must be open for a while
		 *	determined on the opening call or 1 minute by default);
		 **/
		if(pollingStation.getStartPeriod() == null && pollingStation.getClosePeriod() == null) {
			pollingStation.setStartPeriod(LocalDateTime.now());
			pollingStation.setClosePeriod(pollingStation.getStartPeriod().plusMinutes(1));
		}
		
		if(pollingStation.getStartPeriod() == null && pollingStation.getClosePeriod() != null) {			
			pollingStation.setStartPeriod(pollingStation.getClosePeriod().minusMinutes(1));
		}
		
		if(pollingStation.getStartPeriod() != null && pollingStation.getClosePeriod() == null) {
			pollingStation.setClosePeriod(pollingStation.getStartPeriod().plusMinutes(1));
		}
		//End Rule 
		
		pollingStationRepository.save(pollingStation);
		log.info(String.format("PollingStationFindTest id[%d] title[%s] created!", pollingStation.getId(), pollingStation.getTopic()));
	}
	
	public void registerVote(PollingStation pollingStation, Vote vote) {
		validateRules(pollingStation);
		
		
		if (pollingStation.getVoteList()!=null) {
			pollingStation.getVoteList().add(vote);
		}
		else {
			pollingStation.setVoteList(new ArrayList<>());
			pollingStation.getVoteList().add(vote);
		}
		pollingStationRepository.save(pollingStation);		
	}	

	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		
		PollingStation pollingStation = (PollingStation) object;
		
		
		if(pollingStation == null) {
			throw new ChallengeException("A sessão de votação não pode ser nula!");
		}
		
		if(pollingStation.getTopic() == null) {
			throw new ChallengeException("Campo 'Topic' é obrigatório!");
		}
		
		if(pollingStation.getStartPeriod() != null && pollingStation.getClosePeriod() != null && pollingStation.getStartPeriod().isAfter(pollingStation.getClosePeriod())) {
			throw new ChallengeException("A data 'Start Period' não pode ser maior que a data 'Close Period'!");
		}
		
	}

}
