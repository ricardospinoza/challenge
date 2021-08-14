package com.cwi.cooperative.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwi.cooperative.voting.model.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	public Member findByCpf(String cpf);	
}