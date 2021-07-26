package com.cwi.cooperative.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.client.CPFClient;
import com.cwi.cooperative.voting.model.Topic;
import com.cwi.cooperative.voting.repository.TopicRepository;
import com.cwi.cooperative.voting.response.CPFResponse;
import com.cwi.cooperative.voting.response.StatusOfVote;

import ch.qos.logback.core.status.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TopicSaveService {
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private CPFClient cpfClient;
	
	public void execute(Topic topic) {
		CPFResponse cpfResponse = cpfClient.getCPF("00793817066");
		log.info(String.format("Status of Vote [%s]", cpfResponse.getStatusOfVote().toString()));
		if(cpfResponse.getStatusOfVote().equals(StatusOfVote.ABLE_TO_VOTE.toString())) {
			topicRepository.save(topic);			
		}
	}

}
