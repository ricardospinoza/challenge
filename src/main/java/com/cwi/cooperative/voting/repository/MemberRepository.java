package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cwi.cooperative.voting.model.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
	
}