/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.graphs;

import io.cuppajoe.collections.mutable.base.MapEntry;
import io.cuppajoe.collections.mutable.hashtables.HashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

public class WeightedRelationSet<N, W> extends RelationSet<N> implements WeightedRelation<N, W> {

	private HashMap<N, W> weightedRelations = new HashMap<>();
	
	private HashMap<W, Relation<N>> reverseRelations = new HashMap<>();

	@Override
	public void addLeapRelation(N node) {
		addRelation(node);
	}

	@Override
	public void addWeightedRelation(N node, W weight) {
		weightedRelations.put(node, weight);
        var nodes = reverseRelations.get(weight);
		if (nodes == null) {
			nodes = new RelationSet<>();
			reverseRelations.put(weight, nodes);
		}
		nodes.addRelation(node);
		size++;
	}

	@Override
	public void removeRelation(N node) {
		remove(node);
	}

	@Override
	public boolean remove(Object o) {
        var before = size();
        var weight = weightedRelations.get(o);
		if (weight != null) {
			weightedRelations.remove(o);
			reverseRelations.get(weight).remove(o);
		}
		super.remove(o);
		return size() < before;
	}

	@Override
	public boolean isRelatedTo(N node) {
		return contains(node);
	}

	@Override
	public boolean contains(Object o) {
		return super.contains(o) || weightedRelations.containsKey(o);
	}
	
	@Override
	public W getWeight(N node) {
		return weightedRelations.get(node);
	}
	
	public Relation<N> getNodesByWeight(W weight) {
        var result = reverseRelations.get(weight);
		return result == null ? new RelationSet<>() : result;
	}
	
	public Relation<N> getLeapRelations() {
		return this;
	}

	@Override
	public boolean addAll(Collection<? extends N> c) {
		if (c instanceof WeightedRelation) {
            var added = false;
			for (var entry: ((WeightedRelation<N, W>) c).weightedEntries()) {
				if (entry.getValue() == null) {
					addLeapRelation(entry.getKey());
				} else {
					addWeightedRelation(entry.getKey(), entry.getValue());
				}
				added = true;
			}
			return added;
		}
		return super.addAll(c);
	}

	@Override
	public int size() {
		return super.size() + weightedRelations.size();
	}

	@Override
	public void clear() {
		weightedRelations.clear();
		reverseRelations.clear();
		super.clear();
	}

	public Iterable<Entry<N, W>> weightedEntries() {
		return () -> new Iterator<>() {
            Iterator<N> leapIt = WeightedRelationSet.super.iterator();
            Iterator<Entry<N, W>> weightedIt = weightedRelations.entrySet().iterator();
            boolean useLeaps = true;

            @Override
            public boolean hasNext() {
                return useLeaps ? useLeaps = leapIt.hasNext() : weightedIt.hasNext();
            }

            @Override
            public Entry<N, W> next() {
                return useLeaps ? new MapEntry<>(leapIt.next(), null) : weightedIt.next();
            }
        };
	}

	@Override
	public Iterator<N> iterator() {
		return new Iterator<>() {
            Iterator<N> leapIt = WeightedRelationSet.super.iterator();
            Iterator<N> weightedIt = weightedRelations.keySet().iterator();
            boolean useLeaps = true;

            @Override
            public boolean hasNext() {
                return useLeaps ? useLeaps = leapIt.hasNext() : weightedIt.hasNext();
            }

            @Override
            public N next() {
                return useLeaps ? leapIt.next() : weightedIt.next();
            }
        };
	}

	public String toString() {
        var result = new StringBuffer();
		for (var node: weightedEntries()) {
            var arc = " -> " + node.getKey();
            var weight = node.getValue();
			result.append(null == weight ? "empty" + arc + "\n": weight + arc + "\n");
		}
		return result.toString();
	}
}
