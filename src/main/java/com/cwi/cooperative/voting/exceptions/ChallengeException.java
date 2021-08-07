package com.cwi.cooperative.voting.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChallengeException extends RuntimeException {

	private static final long serialVersionUID = 6012044635111833568L;
	
	public ChallengeException(String message) {
		super(message);
	}
}