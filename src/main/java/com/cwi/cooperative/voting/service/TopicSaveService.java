package com.cwi.cooperative.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.model.Topic;
import com.cwi.cooperative.voting.repository.TopicRepository;

@Service
public class TopicSaveService {
	@Autowired
	private TopicRepository topicRepository;
	
	public void execute(Topic topic) {
		topicRepository.save(topic);
	}

}
