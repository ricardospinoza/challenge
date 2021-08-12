package com.cwi.cooperative.voting.service.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.GeraCpfCnpj;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.repository.MemberRepository;

@RunWith(MockitoJUnitRunner.class)
public class MemberSaveServiceTest {		
	
	@InjectMocks
	private MemberSaveService service;

	@Mock
	private MemberRepository repository;
	
	@Test
	public void mustSaveMember() {
		
		Member member = getMemberSuccessful();
		
		service.execute(member);
		
		verify(repository).save(any(Member.class));
	}

	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenNameFieldIsNull() {

		Member member = getMemberSuccessful();
		member.setName(null);

		service.execute(member);

		verify(repository, never()).save(any(Member.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenNameFieldIsEmpty() {

		Member member = getMemberSuccessful();
		member.setName("");

		service.execute(member);

		verify(repository, never()).save(any(Member.class));
	}	
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenNameFieldHasOnlyOneChar() {

		Member member = getMemberSuccessful();
		member.setName("N");

		service.execute(member);

		verify(repository, never()).save(any(Member.class));
	}
	
	@Test(expected = ChallengeException.class)
	public void errorShouldOccurWhenNameFieldHasNotContainsOnlyAlphabet() {
		
		Member member = getMemberSuccessful();

		member.setName(" @Ted 1234");			
		service.execute(member);
		
		verify(repository, never()).save(any(Member.class));
	}
	
	
	private static Member getMemberSuccessful() {
		return Member
				.builder()
				.id(new Random().nextLong())
				.name("Zezinho da silva")
				.cpf("74024992007")
				.build();
	}
	

}
