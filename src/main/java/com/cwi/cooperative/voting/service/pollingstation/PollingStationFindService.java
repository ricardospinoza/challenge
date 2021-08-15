package com.cwi.cooperative.voting.service.pollingstation;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.bean.ResultOfVoteCountBean;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PollingStationFindService implements IValideRules {
	
	@Autowired
	private PollingStationRepository pollingStationRepository;
	
	public PollingStation getById(Long id) {		
		validateRules(id);		
		return pollingStationRepository.getById(id);		
	}

	public List<PollingStation> findAll() {
		return pollingStationRepository.findAll();
	}	

	public boolean isVoteDuplicate(Vote vote) {
		validateRules(vote);
		PollingStation pollingStation = pollingStationRepository.getById(vote.getPollingStation().getId());
		pollingStation.getVoteList();
		for (Vote item: pollingStation.getVoteList()) {
			if (item.getMember().getId() == vote.getMember().getId()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Faz apuração da contagem de votos após o encerramento da sessão de votações 
	 * @param topic
	 * @return Bean com dados da contagem de votos da sessão de votação
	 */	
	public ResultOfVoteCountBean getCountingOfVotes(Topic topic) {
		validateRules(topic);
		PollingStation pollingStation = pollingStationRepository.findByTopic(topic);
		validateRules(pollingStation);
		Map<String, Integer> mpVoteCount = new HashMap<>();
		mpVoteCount.put(Vote.AnswerOptions.YES.name(), 0);
		mpVoteCount.put(Vote.AnswerOptions.NO.name(), 0);		
		for (Vote vote : pollingStation.getVoteList()) {
			mpVoteCount.put(vote.getAnswer(), mpVoteCount.get(vote.getAnswer()) + 1);
		}
		return 
			ResultOfVoteCountBean.builder()
			.topicTitle(pollingStation.getTopic().getTitle())
			.datePolling(pollingStation.getClosePeriod().toLocalDate())
			.totalYes(mpVoteCount.get(Vote.AnswerOptions.YES.name()))
			.totalNo(mpVoteCount.get(Vote.AnswerOptions.NO.name()))
			.build();
	}
	
	/**
	 * Retorna a Sessão de votação correspondente a pauta
	 * @param id - id da pauta
	 * @return Sessão de votação
	 */	
	public PollingStation getByTopic(Topic topic) {
		validateRules(topic);
		return pollingStationRepository.findByTopic(topic);
	}

	/**
	 * Validação de regras de negocio
	 * @param <T>
	 * @param object objeto generico para flexibilidade validação
	 * @throws ChallengeException
	 */
	@Override
	public <T> void validateRules(T object) throws ChallengeException {
		if(object == null) {
			log.error(MessageProperties.get().getMessage("polling-station-object-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-object-invalid"));
		}		
		if(object instanceof PollingStation) {
			PollingStation testRuleVotingInProgress = (PollingStation) object;
			if(LocalDateTime.now().isAfter(testRuleVotingInProgress.getClosePeriod())) {
				log.warn(MessageProperties.get().getMessage("polling-station-voting-in-progress"));
				throw new ChallengeException(MessageProperties.get().getMessage("polling-station-voting-in-progress"));
			}
		}
	}

}
