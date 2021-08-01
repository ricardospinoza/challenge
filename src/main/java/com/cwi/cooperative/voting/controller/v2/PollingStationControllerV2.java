package com.cwi.cooperative.voting.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.Member;
import com.cwi.cooperative.voting.model.PollingStation;
import com.cwi.cooperative.voting.model.ValueOfVote;
import com.cwi.cooperative.voting.service.pollingstation.PollingStationSaveService;
import com.cwi.cooperative.voting.service.vote.VoteSaveService;

@Controller
@RequestMapping("/v2/polling-station")
public class PollingStationControllerV2 {

	@Autowired
	private PollingStationSaveService pollingStationSaveService;
	
	@Autowired
	private VoteSaveService voteSaveService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<PollingStation> add(@RequestBody PollingStation pollingStation) {
		
		try {
			pollingStationSaveService.execute(pollingStation);
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@RequestMapping(value = "/register-vote", method = RequestMethod.POST)
	public ResponseEntity<PollingStation> add(@RequestBody PollingStation pollingStation, @RequestBody Member member, @RequestBody ValueOfVote valueOfVote) {
		
		try {
			//pollingStationSaveService.registerVote(pollingStation, vote);
			voteSaveService.execute(member, pollingStation, valueOfVote);
			
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
}
