package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.Vote;


public interface VoteRepository extends JpaRepository<Vote, Long> {
	
}