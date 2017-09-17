/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.graphs;

public class FiniteAutomatonFactory<L> {
	
	private long stateCount = 0;
	
	public FiniteAutomaton<Long, L> buildFromWordGraph(L[] word) {
		FiniteAutomaton<Long, L> result = new FiniteAutomaton<Long, L>() {
			@Override
			public Long getNewState() {
				return stateCount++;
			}
		};
		Long temp = result.getNewState();
		result.setInitialState(temp);
		if (word.length == 0) return result;
		for (L l: word) {
			result
			.joinStates(temp, temp = result.getNewState(), l)
			.addToLanguage(l);
		}
		return result.addFinalState(temp);
	}
}
