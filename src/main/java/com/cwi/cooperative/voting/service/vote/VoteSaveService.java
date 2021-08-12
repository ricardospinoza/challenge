package com.cwi.cooperative.voting.service.vote;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwi.cooperative.voting.client.CPFClient;
import com.cwi.cooperative.voting.enums.MemberStatusOfVoteEnum;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.VoteRepository;
import com.cwi.cooperative.voting.response.CPFResponse;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationFindService;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationSaveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VoteSaveService implements IValideRules {
	
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private PollingStationFindService pollingStationFindService;
	@Autowired
	private PollingStationSaveService pollingStationSaveService;
	@Autowired
	private CPFClient cpfClient;
	
	/**
	 * Registra o voto do associado na sessão, se o associado está apto para votar e dentro do prazo para votar.
	 * Não permite votar mais de uma vez por cpf
	 * @param member associado
	 * @param pollingStation sessão de votação
	 * @param answer opção de voto sim/não
	 */	
	public void execute(Member member, PollingStation pollingStation, Vote.AnswerOptions answer ) {		
		CPFResponse cpfResponse = cpfClient.getCPF(member.getCpf());
		if(cpfResponse.getMemberStatusOfVoteEnum().equals(MemberStatusOfVoteEnum.ABLE_TO_VOTE)) {
			if (pollingStation.getClosePeriod().isBefore(LocalDateTime.now().plusSeconds(1))) {
				Vote vote = new Vote();
				vote.setMember(member);
				vote.setAnswer(answer.name());
				vote.setPollingStation(pollingStation);
				if (!pollingStationFindService.isVoteDuplicate(vote)) {
					validateRules(vote);
					voteRepository.save(vote);
					pollingStationSaveService.registerVote(pollingStation, vote);
				}
			}		
		}		
		log.info(String.format(MessageProperties.get().getMessage("vote-save"), cpfResponse.getMemberStatusOfVoteEnum().toString()));
	}

	/**
	 * Validação de regras de negocio
	 * @param <T>
	 * @param object objeto generico para flexibilidade validação
	 * @throws ChallengeException
	 */
	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		Vote vote = (Vote) object;		
		if (vote == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("vote-nullo"));
		}		
		if (vote.getMember() == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("vote-member-nullo"));
		}		
		if (vote.getPollingStation() == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("vote-polling-station-nullo"));
		}		
		if (vote.getAnswer() == null) {
			throw new ChallengeException(MessageProperties.get().getMessage("vote-answer-invalid"));
		}		
		if (pollingStationFindService.isVoteDuplicate(vote)) {			
			throw new ChallengeException(MessageProperties.get().getMessage("vote-muiltple-votes"));
		}
	}
}
