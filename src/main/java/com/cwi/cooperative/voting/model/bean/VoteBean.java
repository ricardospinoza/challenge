package com.cwi.cooperative.voting.model.bean;

import com.cwi.cooperative.voting.model.entity.Vote.AnswerOptions;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteBean {
	private Long idTopic;
	private String titleTopic;
	private String cpfMember;
	private AnswerOptions answer;
}