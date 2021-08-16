package com.cwi.cooperative.voting.model.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicBean {
	private Long idTopic;
	private String titleTopic;
}