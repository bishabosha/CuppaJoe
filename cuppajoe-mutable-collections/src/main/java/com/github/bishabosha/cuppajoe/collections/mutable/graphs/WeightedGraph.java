/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.graphs;

public class WeightedGraph<N, W> extends RelationGraph<N, WeightedRelation<N, W>> {

    private N current;

    @Override
    public WeightedRelation<N, W> getNewRelationSet() {
        return new WeightedRelationSet<>();
    }

    public WeightedGraph<N, W> addNodeToCurrent(N node, W weight, boolean isDirected) {
        return joinNodes(current, node, weight, isDirected);
    }

    public WeightedGraph<N, W> joinNodes(N primary, N secondary, W weight, boolean isDirected) {
        addNode(primary).addWeightedRelation(secondary, weight);
        if (isDirected) {
            addNode(secondary);
        } else {
            addNode(secondary).addWeightedRelation(primary, weight);
        }
        current = secondary;
        return this;
    }
}
