package com.cwi.cooperative.voting.controller.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cwi.cooperative.voting.exceptions.ChallengeException;
import com.cwi.cooperative.voting.model.entity.Member;
import com.cwi.cooperative.voting.service.member.MemberSaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/v2/member")
@Api(value = "API Members - V2")
public class MemberControllerV2 {
	@Autowired
	private MemberSaveService memberSaveService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value = "Add Member")
	public ResponseEntity<Member> add(@RequestBody Member member) {
		try {
			memberSaveService.execute(member);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		catch (ChallengeException err) {
			throw new ChallengeException(err.getMessage());
		}
	}
}