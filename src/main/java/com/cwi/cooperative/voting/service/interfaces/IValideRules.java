package com.cwi.cooperative.voting.service.interfaces;

import com.cwi.cooperative.voting.exceptions.ChallengeException;

public interface IValideRules {
	/**
	 * Validação de regras de negocio
	 * @param <T>
	 * @param object objeto generico para flexibilidade validação
	 * @throws ChallengeException
	 */
	public <T> void validateRules(T object) throws ChallengeException;
}
