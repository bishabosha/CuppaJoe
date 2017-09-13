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

    public static abstract class Lockable<E> implements Iterator<E> {
        private boolean hasNext = false;
        private boolean update = true;

        @Override
        public boolean hasNext() {
            if (update) {
                update = false;
                hasNext = hasNextSupplier();
            }
            return hasNext;
        }

        @Override
        public E next() {
            if (update) {
                hasNext();
            }
            if (!update && !hasNext) {
                throw new NoSuchElementException();
            }
            update = true;
            return nextSupplier();
        }

        public abstract boolean hasNextSupplier();

        public abstract E nextSupplier();
    }

    public static <E> Iterable<E> ofSuppliers(Supplier<E>... suppliers) {
        return () -> new Lockable<E>() {

            private int i = 0;

            @Override
            public boolean hasNextSupplier() {
                return i < suppliers.length;
            }

            @Override
            public E nextSupplier() {
                return suppliers[i++].get();
            }
        };
    }

    public static <E> Iterable<E> concat(Iterable<E>... iterables) {
        return () -> new Lockable<E>() {
            Iterator<E> current;
            int i = 0;

            @Override
            public boolean hasNextSupplier() {
                return (current != null && current.hasNext())
                        || getNext();
            }

            public boolean getNext() {
                while (i < iterables.length) {
                    if ((current = iterables[i++].iterator()).hasNext()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public E nextSupplier() {
                return current.next();
            }
        };
    }

    public static <R> R next(Iterator<R> it) {
        return it.hasNext() ? it.next() : null;
    }

    public static <R> Iterable<R> iterate(R identity, UnaryOperator<R> accumulator) {
        return iterate(identity, x -> false, accumulator);
    }

    public static <R> Iterable<R> iterate(R identity,
                                          Predicate<R> terminatingCondition,
                                          UnaryOperator<R> accumulator) {
        return () -> new Lockable<R>() {
            private R current = identity;

            @Override
            public boolean hasNextSupplier() {
                return !terminatingCondition.test(current);
            }

            public R nextSupplier() {
                R result = current;
                current = accumulator.apply(current);
                return result;
            }
        };
    }

    public static <R> Iterable<R> fromOptional(Optional<R> optional) {
        return () -> new Iterator<R>() {

            private boolean unWrapped = false;

            @Override
            public boolean hasNext() {
                return !unWrapped && optional.isPresent();
            }

            @Override
            public R next() {
                if (unWrapped) {
                    throw new NoSuchElementException();
                }
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
        return () -> new Lockable<R>() {

            private int i = 0;

            @Override
            public boolean hasNextSupplier() {
                return i < values.length;
            }

            @Override
            public R nextSupplier() {
                return values[i++];
            }
        };
    }

    public static <R> Iterable<R> cycle(R... values) {
        return cycle(of(values));
    }

    public static <R> Iterable<R> cycle(Iterable<R> source) {
        return () -> new Lockable<R>() {

            Iterator<R> it = getIt();

            @Override
            public boolean hasNextSupplier() {
                return it.hasNext() || (it = getIt()).hasNext();
            }

            @Override
            public R nextSupplier() {
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
}
