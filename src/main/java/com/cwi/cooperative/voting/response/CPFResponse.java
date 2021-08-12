package com.cwi.cooperative.voting.response;

import com.cwi.cooperative.voting.enums.MemberStatusOfVoteEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CPFResponse {
	private MemberStatusOfVoteEnum memberStatusOfVoteEnum;
}
