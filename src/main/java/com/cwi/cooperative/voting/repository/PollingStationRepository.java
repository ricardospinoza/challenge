package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.PollingStation;


public interface PollingStationRepository extends JpaRepository<PollingStation, Long> {
	
}