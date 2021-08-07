package com.cwi.cooperative.voting.service.topic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.repository.TopicRepository;

@RunWith(MockitoJUnitRunner.class)
public class TopicSaveServiceTest {		
	
	@InjectMocks
	private TopicSaveService service;

	@Mock
	private TopicRepository repository;
	
	@Test
	public void mustSaveTopic() {
		
		Topic topic = getTopicSuccessful();
		
		service.execute(topic);
		
		verify(repository).save(any(Topic.class));
	}

	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenTitleFieldIsNull() {

		Topic topic = getTopicSuccessful();
		topic.setTitle(null);

		service.execute(topic);

		verify(repository, never()).save(any(Topic.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenTitleFieldIsEmpty() {

		Topic topic = getTopicSuccessful();
		topic.setTitle("");

		service.execute(topic);

		verify(repository, never()).save(any(Topic.class));
	}	
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenTitleFieldHasOnlyOneChar() {

		Topic topic = getTopicSuccessful();
		topic.setTitle("N");

		service.execute(topic);

		verify(repository, never()).save(any(Topic.class));
	}
	
	
	private static Topic getTopicSuccessful() {
		return Topic
				.builder()
				.id(new Random().nextLong())
				.title("Pauta #1")
				.description("Descricação da Pauta #1")
				.build();
	}
	

}
