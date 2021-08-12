package com.cwi.cooperative.voting.service.pollingstation;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
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
	@Autowired
	private PollingStationFindService pollingStationFindService;
	
	/**
	 * Registra sessão de votação, se não for informado data de abertura e fechamento por padrão a sessão tem duração de 1 minuto
	 * @param pollingStation
	 */
	public void execute(PollingStation pollingStation) {
		validateRules(pollingStation);
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
		pollingStationRepository.save(pollingStation);
		log.info(String.format(MessageProperties.get().getMessage("polling-station-save"), pollingStation.getId(), pollingStation.getTopic()));
	}
	
	//TODO delegar responsabilidade de duplicidade para este método
	public void registerVote(PollingStation pollingStation, Vote vote) {
		validateRules(pollingStation);
		if (!pollingStationFindService.isVoteDuplicate(vote)) {
			
		}
		if (pollingStation.getVoteList()!=null) {
			pollingStation.getVoteList().add(vote);
		}
		else {
			pollingStation.setVoteList(new ArrayList<>());
			pollingStation.getVoteList().add(vote);
		}
		pollingStationRepository.save(pollingStation);
	}

	/**
	 * Validação de regras de negocio
	 * @param <T>
	 * @param object objeto generico para flexibilidade validação
	 * @throws ChallengeException
	 */
	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		PollingStation pollingStation = (PollingStation) object;
		if(pollingStation == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-voting-null"));
		}
		if(pollingStation.getTopic() == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-topic-null"));
		}
		if(pollingStation.getStartPeriod() != null && pollingStation.getClosePeriod() != null && pollingStation.getStartPeriod().isAfter(pollingStation.getClosePeriod())) {
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-start-close-period-invalid"));
		}
	}
}
