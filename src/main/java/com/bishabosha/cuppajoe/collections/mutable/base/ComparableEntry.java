/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.base;

public class ComparableEntry<K extends Comparable<K>, V>
		extends MapEntry<K, V>
		implements Comparable<ComparableEntry<K, V>>{

	public ComparableEntry(K key, V value) {
		super(key, value);
	}

	@Override
	public int compareTo(ComparableEntry<K, V> o) {
		return getKey().compareTo(o.getKey());
	}
}
