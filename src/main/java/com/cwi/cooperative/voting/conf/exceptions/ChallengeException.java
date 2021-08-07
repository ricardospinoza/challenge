package com.cwi.cooperative.voting.conf.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class ChallengeException extends RuntimeException{

	private static final long serialVersionUID = -6806029002466564899L;

	private String message;

	public ChallengeException() {}

	public ChallengeException(String message) {
		this.message = message;
	}
	
}
