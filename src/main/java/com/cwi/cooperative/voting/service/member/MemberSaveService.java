package com.cwi.cooperative.voting.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.conf.helpers.DocumentUtils;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.repository.MemberRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;

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
		
		if(object == null) {
			throw new ChallengeException("Parametro de pesquisa não pode ser nulo!");
		}
		
		Member member = (Member) object;
		
		if (member.getCpf() == null ) {
			throw new ChallengeException("O Campo 'CPF' é obrigatório!");			
		}
		
		if (!DocumentUtils.isValidCPF(member.getCpf())) {
			throw new ChallengeException(String.format("O Campo 'CPF' [%s]  é inválido!", member.getCpf()));			
		}
		
		if (member.getName() == null ) {
			throw new ChallengeException("O Campo 'Name' é obrigatório!");			
		}
		
		if (member.getName().trim().isEmpty() ) {
			throw new ChallengeException("O Campo 'Name' não pode ser vazio!");			
		}
		
		if (member.getName().length() == 1 ) {
			throw new ChallengeException("O Campo 'Name' não ter apenas 1 caractere!");			
		}
		
		if (!member.getName().matches("^[a-zA-Z ]*$") ) {
			throw new ChallengeException("O Campo 'Name' não é válido, pois deve conter apenas letras e espaço!");			
		}
		
	}

}
