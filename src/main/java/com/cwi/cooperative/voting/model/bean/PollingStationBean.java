package com.cwi.cooperative.voting.model.bean;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PollingStationBean {
	private LocalDateTime startPeriod;
	private LocalDateTime closePeriod;
	private Long idTopic;
	private String titleTopic;
}