/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.hashtables;

import com.github.bishabosha.cuppajoe.collections.mutable.base.AbstractMap;
import com.github.bishabosha.cuppajoe.collections.mutable.base.MapEntry;

public class HashMap<K, V> extends AbstractMap<K, V> {

    {
        data = new HashTable<MapEntry<K, V>>();
    }

    private HashTable<Entry<K, V>> getTable() {
        return (HashTable<Entry<K, V>>) data;
    }

    @Override
    public V put(K key, V value) {
        var entry = getTable().replace(new MapEntry<>(key, value), MapEntry::entryEntryEquator);
        return entry == null ? null : entry.getValue();
    }

    public V get(Object key) {
        var entry = getTable().get(key, MapEntry::objectEntryEquator);
        return null == entry ? null : entry.getValue();
    }

    public V remove(Object o) {
        var old = getTable().pull(o, MapEntry::objectEntryEquator);
        return null == old ? null : old.getValue();
    }

    public int numCollisions() {
        return getTable().numCollisions();
    }

    public float currentLoad() {
        return getTable().currentLoad();
    }
}