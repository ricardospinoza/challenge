package com.cwi.cooperative.voting.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.bean.PollingStationBean;
import com.cwi.cooperative.voting.model.bean.ResultOfVoteCountBean;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.model.entity.PollingStation;
import com.cwi.cooperative.voting.model.entity.Topic;
import com.cwi.cooperative.voting.model.entity.Vote;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationFindService;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationSaveService;
import com.cwi.cooperative.voting.service.vote.VoteSaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/v2/polling-station")
@Api(value = "API Polling Station - V2")
public class PollingStationControllerV2 {
	@Autowired
	private PollingStationSaveService pollingStationSaveService;	
	@Autowired
	private PollingStationFindService pollingStationFindService;	
	@Autowired
	private VoteSaveService voteSaveService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "add Polling Station")
	public ResponseEntity<PollingStation> add(@RequestBody PollingStationBean pollingStationBean) {		
		try {			
			PollingStation pollingStation = pollingStationSaveService.prepareToSave(pollingStationBean);
			pollingStationSaveService.execute(pollingStation);
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {
			log.error(String.format("[%s]->%s", this.getClass().getName(), err.getMessage()));
			throw new ChallengeException(err.getMessage());
		}		
	}
	
	@RequestMapping(value = "/register-vote", method = RequestMethod.POST)
	@ApiOperation(value = "add Vote for Polling Station")
	public ResponseEntity<PollingStation> add(@RequestBody PollingStation pollingStation, @RequestBody Member member, @RequestBody Vote.AnswerOptions answer) {		
		try {
			voteSaveService.execute(member, pollingStation, answer);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		catch (ChallengeException err) {
			log.error(String.format("[%s]->%s", this.getClass().getName(), err.getMessage()));
			throw new ChallengeException(err.getMessage());
		}		
	}
	
	@RequestMapping(value = "/result-vote-count", method = RequestMethod.GET)
	@ApiOperation(value = "Result of Vote Count")
	public ResponseEntity<ResultOfVoteCountBean> getResultVoteCount(@RequestBody Topic topic) {		
		try {			 
			ResultOfVoteCountBean voteCount = pollingStationFindService.getCountingOfVotes(topic);			
			return new ResponseEntity<ResultOfVoteCountBean>(voteCount,HttpStatus.OK);			
		}
		catch (ChallengeException err) {
			log.error(String.format("[%s]->%s", this.getClass().getName(), err.getMessage()));
			throw new ChallengeException(err.getMessage());
		}		
	}	
}