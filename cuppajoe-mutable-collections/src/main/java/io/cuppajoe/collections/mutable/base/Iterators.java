package io.cuppajoe.collections.mutable.base;

import io.cuppajoe.collections.mutable.hashtables.HashTable;
import io.cuppajoe.control.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static io.cuppajoe.API.Some;

public final class Iterators {

    public static <K, V> Iterable<Map.Entry<K, V>> mapEntryIteratorSortedKeys(Map<K, V> map, List<K> keys) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(keys);
        var sorted = new ArrayList<Entry<K, V>>();
        var found = new HashTable<K>();
        for (var key : keys) {
            Option.of(key)
                    .filter(map.keySet()::contains)
                    .map(k -> {
                        found.add(k);
                        return new MapEntry<>(k, map.get(k));
                    })
                    .peek(sorted::add);
        }
        for (var entry : map.entrySet()) {
            Some(entry)
                    .filter(e -> !found.contains(e.getKey()))
                    .peek(sorted::add);
        }
        return sorted;
    }
}
