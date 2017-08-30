/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.base;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple2;
import com.bishabosha.caffeine.hashtables.HashMap;
import com.bishabosha.caffeine.hashtables.HashTable;
import com.bishabosha.caffeine.lists.*;
import com.bishabosha.caffeine.lists.LinkedList;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;

public class Iterables {

    public static <R> R next(Iterator<R> it) {
        return it.hasNext() ? it.next() : null;
    }

    public static <R> Iterable<R> iterate(R identity, UnaryOperator<R> accumulator) {
        return iterate(identity, x -> false, accumulator);
    }

    public static <R> Iterable<R> iterate(R identity,
                                          Predicate<R> terminatingCondition,
                                          UnaryOperator<R> accumulator) {
        return () -> new Iterator<R>() {
            R current = identity;

            @Override
            public boolean hasNext() {
                return !terminatingCondition.test(current);
            }

            @Override
            public R next() {
                R result = current;
                current = accumulator.apply(current);
                return result;
            }
        };
    }

    public static <R> Iterable<R> fromOptional(Optional<R> optional) {
        return () -> new Iterator<R>() {

            boolean unWrapped = false;

            @Override
            public boolean hasNext() {
                return !unWrapped && optional.isPresent();
            }

            @Override
            public R next() {
                unWrapped = optional.isPresent();
                return unWrapped ? optional.get() : null;
            }
        };
    }

    public static boolean equalElements(Collection a, Collection b) {
        return a.size() == b.size() && a.containsAll(b) && b.containsAll(a);
    }

    public static <R> boolean equalElements(Collection a, R... values) {
        return equalElements(a, listOf(values));
    }

    public static <R> Set<R> setOf(R... values) {
        Set<R> set = new HashTable<>();
        for (R val: values) {
            set.add(val);
        }
        return set;
    }

    public static <R> List<R> listOf(R... values) {
        List<R> list = new LinkedList<>();
        for (R val: values) {
            list.add(val);
        }
        return list;
    }

    public static <K, V> Map<K, V> mapOf(Tuple2<K, V>... entries) {
        Map<K, V> map = new HashMap<>();
        for (Tuple2<K, V> tuple: entries) {
            map.put(tuple.$1(), tuple.$2());
        }
        return map;
    }

    public static <R> Iterable<R> of(R... values) {
        return wrap(values);
    }

    public static <R> Iterable<R> ofSuppliers(Supplier<R>... values) {
        return () -> new Iterator<R>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public R next() {
                return values[i++].get();
            }
        };
    }

    public static Iterable empty = () -> new Iterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    };


    public static <R> Iterable<R> wrap(R[] values) {
        return () -> new Iterator<R>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public R next() {
                return values[i++];
            }
        };
    }

    public static <R> Iterable<R> cycle(R... values) {
        return cycle(of(values));
    }

    public static <R> Iterable<R> cycle(Iterable<R> source) {
        return () -> new Iterator<R>() {

            Iterator<R> it = getIt();

            @Override
            public boolean hasNext() {
                return it.hasNext() || (it = getIt()).hasNext();
            }

            @Override
            public R next() {
                return it.next();
            }

            private Iterator<R> getIt() {
                return source.iterator();
            }
        };
    }

    public static <K, V> Iterable<Map.Entry<K, V>> mapEntryIteratorSortedKeys(Map<K, V> map, List<K> keys) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(keys);
        List<Map.Entry<K, V>> sorted = new ArrayList<>();
        Set<K> found = new HashTable<>();
        for(K key: keys) {
            Option.ofUnknown(key)
                  .filter(map.keySet()::contains)
                  .ifSome(found::add)
                  .map(k -> new MapEntry<>(k, map.get(k)))
                  .ifSome(sorted::add);
        }
        for(Map.Entry<K, V> entry: map.entrySet()) {
            Option.of(entry)
                  .filter(e -> !found.contains(e.getKey()))
                  .ifSome(sorted::add);
        }
        return sorted;
    }

    public static String toString(char start, char end, Iterator iterator) {
        final StringBuilder builder = new StringBuilder();
        builder.append(start);
        boolean hasNext = iterator.hasNext();
        while (hasNext) {
            builder.append(iterator.next());
            if (hasNext = iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append(end).toString();
    }

    public static <O> String toString (char start, char end, Iterator<O> it, BiConsumer<StringBuilder, O> consumer) {
        StringBuilder builder = new StringBuilder();
        boolean hasNext = it.hasNext();
        builder.append(start);
        O current = hasNext ? it.next() : null;
        while (hasNext) {
            consumer.accept(builder, current);
            hasNext = it.hasNext();
            if (hasNext) {
                current = it.next();
                builder.append(", ");
            }
        }
        builder.append(end);
        return builder.toString();
    }

    public static <O, T extends Collection<O>> T fill(T collection, O[] values) {
        Collections.addAll(collection, values);
        return collection;
    }

    public static <O extends Collection<Integer>> O fill(O collection, int[] values) {
        for (int i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Double>> O fill(O collection, double[] values) {
        for (double i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Float>> O fill(O collection, float[] values) {
        for (float i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Long>> O fill(O collection, long[] values) {
        for (long i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Short>> O fill(O collection, short[] values) {
        for (short i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Byte>> O fill(O collection, byte[] values) {
        for (byte i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Boolean>> O fill(O collection, boolean[] values) {
        for (boolean i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Character>> O fill(O collection, char[] values) {
        for (char i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static Iterable<Integer> wrap(int[] values) {
        return () -> new Iterator<Integer>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Integer next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Long> wrap(long[] values) {
        return () -> new Iterator<Long>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Long next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Float> wrap(float[] values) {
        return () -> new Iterator<Float>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Float next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Character> wrap(char[] values) {
        return () -> new Iterator<Character>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Character next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Double> wrap(double[] values) {
        return () -> new Iterator<Double>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Double next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Short> wrap(short[] values) {
        return () -> new Iterator<Short>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Short next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Boolean> wrap(boolean[] values) {
        return () -> new Iterator<Boolean>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Boolean next() {
                return values[i++];
            }
        };
    }

    public static Iterable<Byte> wrap(byte[] values) {
        return () -> new Iterator<Byte>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public Byte next() {
                return values[i++];
            }
        };
    }
}
