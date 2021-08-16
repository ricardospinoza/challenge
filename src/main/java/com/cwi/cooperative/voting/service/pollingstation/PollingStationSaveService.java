package com.cwi.cooperative.voting.service.pollingstation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.helpers.MessageProperties;
import com.cwi.cooperative.voting.model.bean.PollingStationBean;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.repository.PollingStationRepository;
import com.cwi.cooperative.voting.service.interfaces.IValideRules;
import com.cwi.cooperative.voting.service.topic.TopicFindService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PollingStationSaveService implements IValideRules {
	@Autowired
	private PollingStationRepository pollingStationRepository;
	@Autowired
	private PollingStationFindService pollingStationFindService;
	@Autowired
	private TopicFindService topicFindService;
	
	/**
	 * Registra nova sessão de votação, se não for informado data de abertura e fechamento por padrão a sessão tem duração de 1 minuto
	 * @param pollingStationBean bean com alguns atributos para criação da sessão de votação
	 */
	public void execute(PollingStation pollingStation) {
		validateRules(pollingStation);
		validateRulesforAdd(pollingStation);
		pollingStationRepository.save(pollingStation);
		log.info(String.format(MessageProperties.get().getMessage("polling-station-save"), pollingStation.getId(), pollingStation.getTopic()));
	}
	/**
	 * Registra voto na sessão de votação
	 * @param pollingStation
	 * @param vote
	 */
	public void registerVote(PollingStation pollingStation, Vote vote) {
		validateRules(pollingStation);
		if (!pollingStationFindService.isVoteDuplicate(vote)) {
			if (pollingStation.getVoteList()!=null) {
				pollingStation.getVoteList().add(vote);
			}
			else {
				pollingStation.setVoteList(new ArrayList<>());
				pollingStation.getVoteList().add(vote);
			}
			pollingStationRepository.save(pollingStation);			
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
		PollingStation pollingStation = (PollingStation) object;
		if(pollingStation == null) {
			log.error(MessageProperties.get().getMessage("polling-station-voting-null"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-voting-null"));
		}
		if(pollingStation.getTopic() == null) {
			log.error(MessageProperties.get().getMessage("polling-station-topic-null"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-topic-null"));
		}
		if(pollingStation.getTopic() != null && pollingStation.getTopic().getId() == null) {
			log.error(MessageProperties.get().getMessage("topic-not-found"));
			throw new ChallengeException(MessageProperties.get().getMessage("topic-not-found"));			
		}
		if(pollingStation.getStartPeriod() != null && pollingStation.getClosePeriod() != null && pollingStation.getStartPeriod().isAfter(pollingStation.getClosePeriod())) {
			log.error(MessageProperties.get().getMessage("polling-station-start-close-period-invalid"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-start-close-period-invalid"));
		}
	}	
	private <T> void validateRulesforAdd(T object) throws ChallengeException {
		PollingStation pollingStation = (PollingStation) object;
		if(pollingStationFindService.getByTopic(pollingStation.getTopic()) !=null) {
			log.error(MessageProperties.get().getMessage("polling-station-topic-exits"));
			throw new ChallengeException(MessageProperties.get().getMessage("polling-station-topic-exits"));
		}
	}	
	/**
	 * Prepara (aplica regras de negocio) o objeto pollingStation para ser persistido
	 * @param pollingStationBean
	 * @return
	 */
	public PollingStation prepareToSave(PollingStationBean pollingStationBean) {
		PollingStation pollingStation = new PollingStation();
		Topic topic = null;
		if (pollingStationBean!=null) {
			if (pollingStationBean.getIdTopic()!=null) {
				topic =  topicFindService.getById(pollingStationBean.getIdTopic());
			}
			if (topic == null && pollingStationBean.getTitleTopic()!=null) {
				topic = topicFindService.getByTitle(pollingStationBean.getTitleTopic());
			}
			if (topic == null) {
				log.error(MessageProperties.get().getMessage("topic-not-found"));
				throw new ChallengeException(MessageProperties.get().getMessage("topic-not-found"));
			}
		}
		pollingStation.setTopic(topic);
		pollingStation.setStartPeriod(pollingStationBean.getStartPeriod());
		pollingStation.setClosePeriod(pollingStationBean.getClosePeriod());
		if(pollingStation.getStartPeriod() == null && pollingStation.getClosePeriod() == null) {
			pollingStation.setStartPeriod(LocalDateTime.now());
			pollingStation.setClosePeriod(pollingStation.getStartPeriod().plusMinutes(1));
		}
		if(pollingStation.getStartPeriod() == null && pollingStation.getClosePeriod() != null) {
			pollingStation.setStartPeriod(pollingStation.getClosePeriod().minusMinutes(1));
		}
		if(pollingStation.getStartPeriod() != null && pollingStation.getClosePeriod() == null) {
			pollingStation.setClosePeriod(pollingStation.getStartPeriod().plusMinutes(1));
		}
		return pollingStation;
	}
}
