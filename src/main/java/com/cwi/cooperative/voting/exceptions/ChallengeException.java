package com.cwi.cooperative.voting.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class ChallengeException extends RuntimeException {

	public ChallengeException(String message) {
		super(message);
	}
}