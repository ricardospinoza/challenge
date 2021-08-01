package com.cwi.cooperative.voting.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.Member;
import com.cwi.cooperative.voting.repository.MemberRepository;
import com.cwi.cooperative.voting.service.IValideRules;

//@Slf4j
@Service
public class MemberSaveService implements IValideRules {
	@Autowired
	private MemberRepository memberRepository;
	
	public void execute(Member member) {
		validateRules(member);		
		memberRepository.save(member);		
	}

	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		// TODO Auto-generated method stub
		//TODO validar cpf 
		
	}

}
