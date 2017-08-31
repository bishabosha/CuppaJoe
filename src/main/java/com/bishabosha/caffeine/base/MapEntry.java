/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.base;

import java.util.Map.Entry;
import java.util.Objects;

public class MapEntry<K, V> implements Entry<K, V> {
	private final K key;
	private V value;
	
	public MapEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public static <K, V> boolean entryEntryEquator(Object x, Entry<K, V> y) {
		return ((Entry<K, V>)x).getKey().equals(y.getKey());
	}

	public static <K, V> boolean objectEntryEquator(Object x, Entry<K, V> y) {
		return x.equals(y.getKey());
	}
	
	@Override
	public K getKey() {
		return key;
	}
	
	@Override
	public V getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return key + "=" + value;
	}

	@Override
	public V setValue(V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Entry) {
			final Entry entry = (Entry) obj;
			return Objects.equals(key, entry.getKey()) && Objects.equals(value, entry.getValue());
		}
		return false;
	}
}
