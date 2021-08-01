package com.cwi.cooperative.voting.service.pollingstation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;

@RunWith(MockitoJUnitRunner.class)
public class PollingStationSaveServiceTest {		
	
	@InjectMocks
	private PollingStationSaveService service;

	@Mock
	private PollingStationRepository repository;
	
	@Mock
	private static Topic topic;
	
	@Mock
	private static List<Vote> votes;
	
	@Test
	public void mustSavePollingStation() {
		
		PollingStation pollingStation = getPollingStationSuccessful();
		
		service.execute(pollingStation);
		
		verify(repository).save(any(PollingStation.class));
	}

	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenTopicFieldIsNull() {

		PollingStation pollingStation = getPollingStationSuccessful();
		pollingStation.setTopic(null);

		service.execute(pollingStation);

		verify(repository, never()).save(any(PollingStation.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenFieldStartPeriodGreaterThanClosePeriod() {
		
		PollingStation pollingStation = getPollingStationSuccessful();		
		pollingStation.setClosePeriod(LocalDateTime.now());
		pollingStation.setStartPeriod(pollingStation.getClosePeriod().plusMinutes(10));
		
		service.execute(pollingStation);

		verify(repository, never()).save(any(PollingStation.class));
	}
	
	private static PollingStation getPollingStationSuccessful() {
		return PollingStation
				.builder()
				.id(new Random().nextLong())
				.topic(topic)
				.startPeriod(LocalDateTime.now())
				.closePeriod(LocalDateTime.now().plusMinutes(10))
				.voteList(votes)
				.build();
	}
	

}
