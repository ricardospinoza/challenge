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
	@Autowired
	private TopicFindService topicFindService;
	
	/**
	 * Registra nova pauta no banco de dados
	 * @param topic
	 */	
	public void execute(Topic topic) {
		validateRules(topic);
		topicRepository.save(topic);
		log.info(String.format(MessageProperties.get().getMessage("topic-save"), topic.getId(), topic.getTitle()));
	}

	/**
	 * Validação das regras de negocio
	 * @param <T>
	 * @param object objeto generico para flexibilidade validação
	 * @throws ChallengeException
	 */
	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		Topic topic = (Topic) object;
		if(topic.getTitle() == null) {
			log.error(MessageProperties.get().getMessage("topic-title-null"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-title-null"));
		}
		if(topic.getTitle().trim().isEmpty()) {
			log.error(MessageProperties.get().getMessage("topic-tigle-empty"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-tigle-empty"));
		}
		if(topic.getTitle().length() == 1) {
			log.error(MessageProperties.get().getMessage("topic-length-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-length-invalid"));
		}
		if(topicFindService.getByTitle(topic.getTitle())!= null) {
			log.error(MessageProperties.get().getMessage("topic-duplicated"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-duplicated"));
		}
	}
}
