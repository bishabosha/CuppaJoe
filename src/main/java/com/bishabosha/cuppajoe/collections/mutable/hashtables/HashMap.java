/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.hashtables;

import com.bishabosha.cuppajoe.collections.mutable.base.AbstractMap;
import com.bishabosha.cuppajoe.collections.mutable.base.MapEntry;

public class HashMap<K, V> extends AbstractMap<K, V> {

	{
		data = new HashTable<MapEntry<K, V>>();
	}

	private HashTable<Entry<K, V>> getTable() {
		return (HashTable<Entry<K,V>>) data;
	}

	@Override
	public V put(K key, V value) {
		Entry<K, V> entry = getTable().replace(new MapEntry<>(key, value), MapEntry::entryEntryEquator);
		return entry == null ? null : entry.getValue();
	}
	
	public V get(Object key) {
		Entry<K, V> entry = getTable().get(key, MapEntry::objectEntryEquator);
		return null == entry ? null : entry.getValue();
	}
	
	public V remove(Object o) {
		Entry<K, V> old = getTable().pull(o, MapEntry::objectEntryEquator);
		return null == old ? null : old.getValue();
	}

	public int numCollisions() {
		return getTable().numCollisions();
	}

	public float currentLoad() {
		return getTable().currentLoad();
	}
}