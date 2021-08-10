package com.cwi.cooperative.voting.service.member;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.GeraCpfCnpj;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.repository.MemberRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Configuration
public class MemberSaveService implements IValideRules {
	
	@Autowired
	private MemberRepository memberRepository;
	
	public void execute(Member member) {
		validateRules(member);		
		memberRepository.save(member);
	}
	
	public void executeRandomMember() {		
		Member random = Member
		.builder()
		.id(new Random().nextLong())
		.name("Random Member")
		.cpf(new GeraCpfCnpj().cpf(false))
		.build();		
		validateRules(random);		
		memberRepository.save(random);		
	}

	@Override
	public <T> void validateRules(T object) throws ChallengeException {		
		
		if(object == null) {
			log.error(MessageProperties.get().getMessage("member-object-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-object-invalid"));
		}		
		Member member = (Member) object;		
		if (member.getCpf() == null ) {
			log.error(MessageProperties.get().getMessage("member-object-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-object-invalid"));			
		}
		if (member.getName() == null ) {
			log.error(MessageProperties.get().getMessage("member-name-null"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-name-null"));			
		}		
		if (member.getName().trim().isEmpty() ) {
			log.error(MessageProperties.get().getMessage("member-name-empty"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-name-empty"));			
		}		
		if (member.getName().length() == 1 ) {
			log.error(MessageProperties.get().getMessage("member-name-length-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-name-length-invalid"));
		}
		if (!member.getName().matches("^[a-zA-Z ]*$") ) {
			log.error(MessageProperties.get().getMessage("member-name-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("member-name-invalid"));			
		}		
	}

}
