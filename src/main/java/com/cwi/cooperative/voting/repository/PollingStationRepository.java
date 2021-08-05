package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.entity.PollingStation;


public interface PollingStationRepository extends JpaRepository<PollingStation, Long> {
	
	public PollingStation findByTopic(Long id);
	
}