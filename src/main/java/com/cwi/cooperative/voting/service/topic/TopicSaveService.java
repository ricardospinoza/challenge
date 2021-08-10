package com.cwi.cooperative.voting.service.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.repository.TopicRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopicSaveService implements IValideRules {
	@Autowired
	private TopicRepository topicRepository;
	
	public void execute(Topic topic) {
		validateRules(topic);
		topicRepository.save(topic);
		log.info(String.format(MessageProperties.get().getMessage("topic-save"), topic.getId(), topic.getTitle()));
	}

	@Override
	public <T> void validateRules(T object) throws ChallengeException {		
		Topic topic = (Topic) object;		
		if(topic.getTitle() == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("topic-title-null"));
		}
		if(topic.getTitle().trim().isEmpty()) {
			throw new ChallengeException(MessageProperties.get().getMessage("topic-tigle-empty"));
		}
		if(topic.getTitle().length() == 1) {
			throw new ChallengeException(MessageProperties.get().getMessage("topic-length-invalid"));
		}		
	}
}
