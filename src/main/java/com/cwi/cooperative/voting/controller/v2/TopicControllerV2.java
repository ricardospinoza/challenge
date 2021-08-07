package com.cwi.cooperative.voting.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.service.topic.TopicSaveService;

@Controller
@RequestMapping("/v2/topic")
public class TopicControllerV2 {

	@Autowired
	private TopicSaveService topicSaveService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Topic> add(@RequestBody Topic topic) {
		try {
			topicSaveService.execute(topic);
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
