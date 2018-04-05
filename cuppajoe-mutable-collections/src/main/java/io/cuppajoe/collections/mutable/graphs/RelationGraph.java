/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.graphs;

import io.cuppajoe.collections.mutable.hashtables.HashMap;

public abstract class RelationGraph<N, R extends Relation<N>> {

    protected HashMap<N, R> relations = new HashMap<>();

    protected N current;
    protected long size = 0;

    public long size() {
        return size;
    }

    public N getCurrent() {
        return current;
    }

    public R getRelations() {
        return relations.get(current);
    }

    public R getRelations(N node) {
        return relations.get(node);
    }

    public abstract R getNewRelationSet();

    public R addNode(N node) {
        var relation = getRelations(node);
        if (null != relation) {
            return relation;
        }
        relation = getNewRelationSet();
        relations.put(node, relation);
        current = node;
        size++;
        return relation;
    }

    public RelationGraph<N, R> addNodeToCurrent(N node, boolean isDirected) {
        return joinNodes(current, node, isDirected);
    }

    public RelationGraph<N, R> joinNodes(N primary, N secondary, boolean isDirected) {
        addNode(primary).addRelation(secondary);
        if (isDirected) addNode(secondary);
        else addNode(secondary).addRelation(primary);
        current = secondary;
        return this;
    }

    public boolean areConnected(N primary, N secondary) {
        return relations.get(primary).isRelatedTo(secondary);
    }

    public RelationGraph<N, R> removeNode(N node) {
        for (var connection : getRelations(node)) {
            removeConnection(connection, node);
        }
        relations.remove(node);
        return this;
    }

    public RelationGraph<N, R> removeConnection(N from, N toRemove) {
        relations.get(from).removeRelation(toRemove);
        return this;
    }

    public void printRelations() {
        for (var node : relations.keySet()) {
            System.out.println("Relations for " + node + ":");
            System.out.println(relations.get(node));
        }
    }
}
