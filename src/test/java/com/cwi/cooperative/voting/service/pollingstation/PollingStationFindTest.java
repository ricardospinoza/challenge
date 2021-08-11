package com.cwi.cooperative.voting.service.pollingstation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.repository.PollingStationRepository;

@RunWith(MockitoJUnitRunner.class)
public class PollingStationFindTest {
	
	@InjectMocks	
	private PollingStationFindService service;

	@Mock
	private PollingStationRepository repository;
	
	@InjectMocks
	private Topic topic;
	
	@InjectMocks
	private PollingStation pollingStation;
		
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenIdIsNull() {

		service.getById(null);

		verify(repository, never()).save(any(PollingStation.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenTopicFieldIsNull() {
		
		service.getByTopic(null);

		verify(repository, never()).save(any(PollingStation.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorItMustGiveAnErrorInTheCountingOfVotesWhileVotingIsInProgress() {
		topic.setId(1L);
		try {
			service.getCountingOfVotes(topic);
		}
		catch (ChallengeException erroOk) {
			pollingStation.setClosePeriod(LocalDateTime.now().minusMinutes(1));		
			service.validateRules(pollingStation);
		}
		
		verify(repository, never()).save(any(PollingStation.class));
	}
	
	
}
