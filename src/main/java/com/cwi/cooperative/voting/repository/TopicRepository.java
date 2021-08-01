package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.entity.Topic;


public interface TopicRepository extends JpaRepository<Topic, Long> {
	
}