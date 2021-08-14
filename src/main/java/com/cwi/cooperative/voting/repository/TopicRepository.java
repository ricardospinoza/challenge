package com.cwi.cooperative.voting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwi.cooperative.voting.model.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
	public Topic findByTitle(String title);
	public Optional<Topic> findById(Long id);
}