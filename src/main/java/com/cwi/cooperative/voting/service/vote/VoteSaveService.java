package com.cwi.cooperative.voting.service.vote;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwi.cooperative.voting.client.CPFClient;
import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.model.enums.ValueOfVote;
import com.cwi.cooperative.voting.repository.VoteRepository;
import com.cwi.cooperative.voting.response.CPFResponse;
import com.cwi.cooperative.voting.response.MemberStatusOfVote;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationFindService;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationSaveService;

//@Slf4j
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
	
	public void execute(Member member, PollingStation pollingStation, ValueOfVote valueOfVote ) {
		
		//
		CPFResponse cpfResponse = cpfClient.getCPF(member.getCpf());
		if(cpfResponse.getMemberStatusOfVote().equals(MemberStatusOfVote.ABLE_TO_VOTE.toString())) {
			if (pollingStation.getClosePeriod().isBefore(LocalDateTime.now().plusSeconds(1))) {
				Vote vote = new Vote();
				vote.setMember(member);
				vote.setValue(valueOfVote);
				vote.setPollingStation(pollingStation);
				if (!pollingStationFindService.isVoteDuplicate(vote)) {
					validateRules(vote);
					voteRepository.save(vote);
					pollingStationSaveService.registerVote(pollingStation, vote);
				}
			}		
		}		
		//log.info(String.format("Status of Vote [%s]", cpfResponse.getStatusOfVote().toString()));
	}

	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		Vote vote = (Vote) object;
		
		if (vote == null) {
			throw new ChallengeException("O Voto não pode ser nulo!");
		}
		
		if (vote.getMember() == null) {
			throw new ChallengeException("O associado não pode ser nulo!");
		}
		
		if (vote.getPollingStation() == null) {
			throw new ChallengeException("A sessão de votação não pode nula!");
		}
		
		if (vote.getValue() == null) {
			throw new ChallengeException("Valor de volto inválido!");
		}
		
		if (pollingStationFindService.isVoteDuplicate(vote)) {
			throw new ChallengeException("Erro, não é possível votar mais de uma vez na mesma sessão de votações!");
		}
		
		
	}

}
