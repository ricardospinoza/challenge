package com.cwi.cooperative.voting.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.service.member.MemberSaveService;

@Controller
@RequestMapping("/v2/member")
public class MemberControllerV2 {

	@Autowired
	private MemberSaveService memberSaveService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Member> add(@RequestBody Member member) {
		try {
			memberSaveService.execute(member);
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {	
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			//return ResponseEntity.badRequest().body(err);
		}
	}
	
	@RequestMapping(value = "/add-radom", method = RequestMethod.POST)
	public ResponseEntity<Member> add() {
		try {
			memberSaveService.executeRandomMember();
			return ResponseEntity.status(HttpStatus.OK).build();			
		}
		catch (ChallengeException err) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
}
