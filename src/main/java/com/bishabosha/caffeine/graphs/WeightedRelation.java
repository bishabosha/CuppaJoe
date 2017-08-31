/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.graphs;

import java.util.Map.Entry;

public interface WeightedRelation<N, W> extends Relation<N>{
	void addWeightedRelation(N node, W weight);
	void addLeapRelation(N node);
	W getWeight(N node);
	Relation<N> getNodesByWeight(W weight);
	Relation<N> getLeapRelations();
	Iterable<Entry<N, W>> weightedEntries();
}
