package com.cwi.cooperative.voting.service.pollingstation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.beans.ResultOfVoteCountBean;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;

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

	public boolean isVoteDuplicate(Vote vote) {
		
		validateRules(vote);		
		PollingStation pollingStation = pollingStationRepository.getById(vote.getPollingStation().getId());		
		return pollingStation.getVoteList().contains(vote);
	}	
	
	public ResultOfVoteCountBean getCountingOfVotes(Topic topic) {		
	
		validateRules(topic);
		
		PollingStation pollingStation = pollingStationRepository.findByTopic(topic.getId());
		
		Map<String, Integer> mpVoteCount = new HashMap<>();
		mpVoteCount.put(Vote.VoteAnswerEnum.YES.name(), 0);
		mpVoteCount.put(Vote.VoteAnswerEnum.NO.name(), 0);
		
		for (Vote vote : pollingStation.getVoteList()) {
			mpVoteCount.put(vote.getValue(), mpVoteCount.get(vote.getValue()) + 1);
		}
		
		return 
			ResultOfVoteCountBean.builder()
			.topicTitle(pollingStation.getTopic().getTitle())
			.datePolling(pollingStation.getClosePeriod().toLocalDate())
			.totalYes(mpVoteCount.get(Vote.VoteAnswerEnum.YES.name()))
			.totalNo(mpVoteCount.get(Vote.VoteAnswerEnum.NO.name()))
			.build();		
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
