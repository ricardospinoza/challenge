package com.cwi.cooperative.voting.service.pollingstation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.PollingStation;
import com.cwi.cooperative.voting.repository.PollingStationRepository;

@RunWith(MockitoJUnitRunner.class)
public class PollingStationFindTest {
	
	@InjectMocks	
	private PollingStationFindService service;

	@Mock
	private PollingStationRepository repository;
	
		
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
	
}
