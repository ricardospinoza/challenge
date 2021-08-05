package com.cwi.cooperative.voting.model.beans;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultOfVoteCountBean {
	
	private String topicTitle;
	private LocalDate datePolling;
	private Integer totalYes;
	private Integer totalNo;
	
}
