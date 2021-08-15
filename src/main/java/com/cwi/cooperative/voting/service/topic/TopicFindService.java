package com.cwi.cooperative.voting.service.topic;

import java.util.List;
import java.util.Optional;
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
public class TopicFindService implements IValideRules {
	@Autowired
	private TopicRepository topicRepository;
	
	/**
	 * Pesquisa pauta por id
	 * @param id
	 * @return
	 */
	public Topic getById(Long id) {		 
		validateRules(id);		
		Optional<Topic> op =  Optional.ofNullable(topicRepository.findById(id)).orElse(null);
		return (op.isPresent()?op.get():null);
	}
	/**
	 * Pesquisa de pauta por titulo
	 * @param title - titulo da pauta
	 * @return
	 */
	public Topic getByTitle(String title) {
		validateRules(title);
		return topicRepository.findByTitle(title);
	}
	/**
	 * Pesquisa geral de pautas
	 * @return
	 */
	public List<Topic> findAll() {
		return topicRepository.findAll();
	}	
	/**
	 * Valida regras de negócios
	 */
	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		if(object == null) {
			log.error(MessageProperties.get().getMessage("topic-object-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-object-invalid"));
		}
	}
}
