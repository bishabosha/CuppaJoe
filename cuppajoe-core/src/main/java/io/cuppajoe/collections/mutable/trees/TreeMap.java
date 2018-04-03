/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.trees;

import io.cuppajoe.collections.mutable.base.AbstractMap;
import io.cuppajoe.collections.mutable.base.ComparableEntry;
import io.cuppajoe.collections.mutable.base.MapEntry;

public class TreeMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {

	{
		data = new SearchTree<ComparableEntry<K, V>>();
	}

	private SearchTree<ComparableEntry<K, V>> getTree() {
		return (SearchTree<ComparableEntry<K, V>>) data;
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			return getTree().contains(new ComparableEntry((Comparable) key, null), MapEntry::entryEntryEquator);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public V put(K key, V value) {
		var old = getTree().replace(new ComparableEntry<>(key, value), MapEntry::entryEntryEquator);
		return null == old ? null : old.getValue();
	}

	@Override
	public V get(Object key) {
		if (key instanceof Comparable<?> == false) {
			return null;
		}
		try {
			var result = getTree().search(new ComparableEntry<>((K)key, null), MapEntry::entryEntryEquator);
			return null == result ? null : result.getValue();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public V remove(Object key) {
		if (key instanceof Comparable<?> == false) {
			return null;
		}
		try {
			var old = getTree().pull(new ComparableEntry<>((K)key, null), MapEntry::entryEntryEquator);
			return null == old ? null : old.getValue();
		} catch (Exception e) {
			return null;
		}
	}
}