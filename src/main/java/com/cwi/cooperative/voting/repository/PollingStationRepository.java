package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;


public interface PollingStationRepository extends JpaRepository<PollingStation, Long> {
	
	public PollingStation findByTopic(Topic topic);
	
}