package com.cwi.cooperative.voting.service.vote;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwi.cooperative.voting.enums.MemberStatusOfVoteEnum;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.bean.VoteBean;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.MemberRepository;
import com.cwi.cooperative.voting.repository.VoteRepository;
import com.cwi.cooperative.voting.resouces.response.CPFResponse;
import com.cwi.cooperative.voting.resources.client.CPFClient;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationFindService;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationSaveService;
import com.cwi.cooperative.voting.service.topic.TopicFindService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VoteSaveService implements IValideRules {
	
	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private TopicFindService topicFindService;
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
	public void execute(VoteBean voteBean) {
		if (voteBean!=null) {
			Topic topic = null;
			if (voteBean.getIdTopic()!=null) {
				topic =  topicFindService.getById(voteBean.getIdTopic());				
			}
			if (topic == null && voteBean.getTitleTopic()!=null) {
				topic = topicFindService.getByTitle(voteBean.getTitleTopic());
			}
			Member member = memberRepository.findByCpf(voteBean.getCpfMember());
			PollingStation pollingStation = pollingStationFindService.getByTopic(topic);
			Vote.AnswerOptions answer = voteBean.getAnswer();
			log.info("Consulting WS CPFResponse...");
			CPFResponse cpfResponse = cpfClient.getCPF(member.getCpf());
			log.info(String.format(MessageProperties.get().getMessage("polling-station-cpf-response"), member.getCpf(),cpfResponse.getStatus()));
			if(cpfResponse.getStatus().equals(MemberStatusOfVoteEnum.ABLE_TO_VOTE.name())) {
				if (LocalDateTime.now().isBefore(pollingStation.getClosePeriod())) {
					Vote vote = new Vote();
					vote.setMember(member);
					vote.setAnswer(answer.name());
					vote.setPollingStation(pollingStation);
					if (!pollingStationFindService.isVoteDuplicate(vote)) {
						validateRules(vote);
						voteRepository.save(vote);
						pollingStationSaveService.registerVote(pollingStation, vote);
					}
					else {
						log.warn(MessageProperties.get().getMessage("vote-muiltple-votes"));
						throw new ChallengeException(MessageProperties.get().getMessage("vote-muiltple-votes"));
					}
				}
				else {
					log.warn(String.format(MessageProperties.get().getMessage("polling-station-timeout"), pollingStation.getClosePeriod()));
					throw new ChallengeException(String.format(MessageProperties.get().getMessage("polling-station-timeout"), pollingStation.getClosePeriod()));
				}
			}
			else {
				log.warn(String.format(MessageProperties.get().getMessage("polling-station-cpf-response"), member.getCpf(),cpfResponse.getStatus()));
				throw new ChallengeException(String.format(MessageProperties.get().getMessage("polling-station-cpf-response"), member.getCpf(),cpfResponse.getStatus()));
			}
			log.info(String.format(MessageProperties.get().getMessage("vote-save"), cpfResponse.getStatus().toString()));
		}
		else {
			log.error(MessageProperties.get().getMessage("polling-station-vote-no-complete"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-vote-no-complete"));
		}
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
