package com.cwi.cooperative.voting.service.interfaces;

import com.cwi.cooperative.voting.exceptions.ChallengeException;

public interface IValideRules {
	public <T> void validateRules(T object) throws ChallengeException;
}
