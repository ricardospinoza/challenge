package com.cwi.cooperative.voting.model;

public enum ValueOfVote {

	YES(true), NO(false);

	private boolean value;

	private ValueOfVote(boolean value) {
		this.value = value;
	}

	public boolean getValue() {
		return this.value;
	}

}
