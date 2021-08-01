package com.cwi.cooperative.voting.service.pollingstation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.PollingStation;
import com.cwi.cooperative.voting.model.Topic;
import com.cwi.cooperative.voting.model.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;
import com.cwi.cooperative.voting.service.IValideRules;

@Service
public class PollingStationFindService implements IValideRules {
	
	@Autowired
	private PollingStationRepository pollingStationRepository;	

	public PollingStation getById(Long id) {	
		
		validateRules(id);
		
		return pollingStationRepository.getById(id);
		
	}

	public List<PollingStation> findAll() {
		return pollingStationRepository.findAll();
	}
	
	
//	public boolean registerVote() {
//		
//	}
	
	public boolean isVoteDuplicate(Vote vote) {
		
		validateRules(vote);
		
//		Example<Vote> target = Example.of(
//				Vote.builder()
//				.member(vote.getMember())
//				.pollingStation(vote.getPollingStation())				
//				.build());
		
		
		//Example<PollingStation> target = Example.of(PollingStation.builder()..topic(vote.getPollingStation().getTopic()).build());
		//Example<PollingStation> target = Example.of(PollingStation.builder().id(vote.getPollingStation().getId()).build());

		//List<PollingStation> pollingStationRepository.findAll(target);
		
		PollingStation pollingStation = pollingStationRepository.getById(vote.getPollingStation().getId());
		
		return pollingStation.getVoteList().contains(vote);
		
		
		
		
	}
	
	public List<PollingStation> getByTopic(Topic topic) {
		
		validateRules(topic);
		
		Example<PollingStation> target = Example.of(PollingStation.builder().topic(topic).build());
		
		return pollingStationRepository.findAll(target);
	}	

	@Override
	public <T> void validateRules(T object) throws ChallengeException {	
		
		if(object == null) {
			throw new ChallengeException("Parametro de pesquisa não pode ser nulo!");
		}		
	}	
	

}
