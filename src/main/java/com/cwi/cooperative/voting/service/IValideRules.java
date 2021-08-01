package com.cwi.cooperative.voting.service;

import com.cwi.cooperative.voting.conf.exceptions.ChallengeException;

public interface IValideRules {
	public <T> void validateRules(T object) throws ChallengeException;
}
