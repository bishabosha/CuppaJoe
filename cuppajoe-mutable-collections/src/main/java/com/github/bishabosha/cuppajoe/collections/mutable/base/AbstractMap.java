/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.base;

import java.util.*;

public abstract class AbstractMap<K, V> implements Map<K, V> {

    protected AbstractSet<? extends Entry<K, V>> data;

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Objects.requireNonNull(m, "m").forEach(this::put);
    }

    @Override
    public boolean containsKey(Object key) {
        for (var entry : data) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (var entry : data) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        var base = entrySet().toString();
        return "{" + base.substring(1, base.length() - 1) + "}";
    }

    @Override
    public void clear() {
        data.clear();
    }

    public Set<K> keySet() {
        return new BasicSet<>() {

            @Override
            public boolean contains(Object o) {
                return containsKey(o);
            }

            @Override
            public boolean remove(Object o) {
                var old = size();
                AbstractMap.this.remove(o);
                return old > size();
            }

            @Override
            public int size() {
                return data.size();
            }

            @Override
            public void clear() {
                data.clear();
            }

            @Override
            public Iterator<K> iterator() {
                return new Iterator<>() {

                    private Iterator<? extends Entry<K, V>> it = data.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public K next() {
                        return it.next().getKey();
                    }
                };
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new BasicSet<>() {
            {
                store = data;
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new BasicSet<>() {

            @Override
            public boolean contains(Object o) {
                return containsValue(o);
            }

            @Override
            public boolean remove(Object o) {
                for (var entry : data) {
                    if (entry.getValue().equals(o)) {
                        data.remove(entry);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int size() {
                return data.size();
            }

            @Override
            public void clear() {
                data.clear();
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<>() {

                    private Iterator<? extends Entry<K, V>> it = data.iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public V next() {
                        return it.next().getValue();
                    }
                };
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof Map && entrySet().equals(((Map) obj).entrySet());
    }
}
