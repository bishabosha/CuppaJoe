/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.graphs;

import java.util.Set;

public interface Relation<N> extends Set<N> {
	void addRelation(N node);
	void removeRelation(N node);
	boolean isRelatedTo(N node);
}
