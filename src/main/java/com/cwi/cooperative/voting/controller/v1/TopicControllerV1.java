package com.cwi.cooperative.voting.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.service.topic.TopicSaveService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/v1/topic")
@Api(value = "API Topic - V1")
public class TopicControllerV1 {
	
	@Autowired
	private TopicSaveService topicSaveService;	
	
	@PostMapping(value="/add")	
	@ApiOperation(value = "Add Topic")
	public ResponseEntity<String> add(
			@RequestParam(required=true) String title,
			@RequestParam(required=false) String description) {
		
		topicSaveService.execute(Topic.builder().title(title).description(description).build());
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
