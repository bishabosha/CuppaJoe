/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.graphs;

public class Graph<N> extends RelationGraph<N, Relation<N>> {
    @Override
    public Relation<N> getNewRelationSet() {
        return new RelationSet<>();
    }
}
