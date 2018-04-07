/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.graphs;

import com.github.bishabosha.cuppajoe.collections.mutable.hashtables.HashTable;

public class RelationSet<N> extends HashTable<N> implements Relation<N> {

    @Override
    public void addRelation(N node) {
        add(node);
    }

    @Override
    public void removeRelation(N node) {
        remove(node);
    }

    @Override
    public boolean isRelatedTo(N node) {
        return contains(node);
    }
}
