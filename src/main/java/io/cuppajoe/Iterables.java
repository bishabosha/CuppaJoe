/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe;

import io.cuppajoe.collections.mutable.base.MapEntry;
import io.cuppajoe.collections.mutable.hashtables.HashTable;
import io.cuppajoe.control.Option;
import io.cuppajoe.tuples.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static io.cuppajoe.API.Some;

public interface Iterables {

    abstract class Lockable<E> implements Iterator<E> {
        private boolean hasNext = false;
        private boolean update = true;

        @Override
        public boolean hasNext() {
            if (update) {
                getUpdate();
            }
            return hasNext;
        }

        @Override
        public E next() {
            if (update) {
                getUpdate();
            }
            if (!update && !hasNext) {
                throw new NoSuchElementException();
            }
            update = true;
            return nextSupplier();
        }

        private void getUpdate() {
            update = false;
            hasNext = hasNextSupplier();
        }

        public abstract boolean hasNextSupplier();

        public abstract E nextSupplier();
    }

    static int hash(Iterable<?> iterable) {
        var hash = 1;
        for (var element : iterable) {
            hash = 29 * hash + (element != null ? element.hashCode() : 0);
        }
        return hash;
    }

    static <E> Iterator<E> singleton(Supplier<? extends E> supplier) {
        return new Iterator<>() {
            boolean unwrapped = false;

            @Override
            public boolean hasNext() {
                return !unwrapped;
            }

            @Override
            public E next() {
                if (unwrapped) {
                    throw new NoSuchElementException();
                }
                unwrapped = true;
                return supplier.get();
            }
        };
    }

    @NotNull
    @Contract(pure = true)
    @SafeVarargs
    static <E> Iterable<E> ofSuppliers(Supplier<E>... suppliers) {
        return () -> new Lockable<>() {
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

    @NotNull
    @Contract(pure = true)
    @SafeVarargs
    static <E> Iterable<E> concat(Iterable<E>... iterables) {
        return () -> new Lockable<>() {
            Iterator<E> current;
            int i = 0;

            @Override
            public boolean hasNextSupplier() {
                return (current != null && current.hasNext())
                        || getNext();
            }

            private boolean getNext() {
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

    @NotNull
    @Contract(pure = true)
    static <R> Iterable<R> iterate(R identity, UnaryOperator<R> accumulator) {
        return iterate(identity, x -> false, accumulator);
    }

    @NotNull
    @Contract(pure = true)
    static <R> Iterable<R> iterate(R identity,
            Predicate<R> terminatingCondition,
            UnaryOperator<R> accumulator) {
        return () -> new Lockable<>() {
            private R current = identity;

            @Override
            public boolean hasNextSupplier() {
                return !terminatingCondition.test(current);
            }

            public R nextSupplier() {
                var result = current;
                current = accumulator.apply(current);
                return result;
            }
        };
    }

    static <R> Iterable<R> fromOptional(Optional<R> optional) {
        return () -> new Iterator<>() {

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

    static <E> boolean equals(Iterable<E> base, Object obj) {
        var it = ((Iterable) obj).iterator();
        for (var term: base) {
            if (!it.hasNext() || !Objects.equals(term, it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    static boolean equalElements(Collection a, Collection b) {
        return a.size() == b.size() && a.containsAll(b) && b.containsAll(a);
    }

    static <R> boolean equalElements(Collection a, R... values) {
        return equalElements(a, listOf(values));
    }

    static <R> Set<R> setOf(R... values) {
        var set = new HashTable<R>();
        for (var val: values) {
            set.add(val);
        }
        return set;
    }

    static <R> List<R> listOf(R... values) {
        var list = new LinkedList<R>();
        for (var val: values) {
            list.add(val);
        }
        return list;
    }

    static <K, V> Map<K, V> mapOf(Tuple2<K, V>... entries) {
        var map = new HashMap<K, V>();
        for (var tuple: entries) {
            map.put(tuple.$1(), tuple.$2());
        }
        return map;
    }

    static <R> Iterator<R> of(R... values) {
        return wrap(values);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    static <O> Iterator<O> empty() {
        return (Iterator<O>) EMPTY_ITERATOR;
    }

    Iterator<Object> EMPTY_ITERATOR = new Iterator<>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }
    };

    static <R> Iterator<R> tupleIterator(Product product) {
        return new Iterator<>() {

            private int i = 1;

            @Override
            public boolean hasNext() {
                return i <= product.arity();
            }

            @Override
            public R next() {
                return (R) product.$(i++);
            }
        };
    }

    static <R> Iterator<R> wrap(R[] values) {
        return new Lockable<>() {

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

    static <R> Iterator<R> cast(Object[] values) {
        return new Lockable<>() {

            private int i = 0;

            @Override
            public boolean hasNextSupplier() {
                return i < values.length;
            }

            @Override
            public R nextSupplier() {
                return (R) values[i++];
            }
        };
    }

    static <R> Iterable<R> cycle(R... values) {
        return cycle(() -> of(values));
    }

    static <R> Iterable<R> cycle(Iterable<R> source) {
        return () -> new Lockable<>() {

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

    static <K, V> Iterable<Map.Entry<K, V>> mapEntryIteratorSortedKeys(Map<K, V> map, List<K> keys) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(keys);
        var sorted = new ArrayList<Map.Entry<K, V>>();
        var found = new HashTable<K>();
        for(var key: keys) {
            Option.of(key)
              .filter(map.keySet()::contains)
              .map(k -> {
                  found.add(k);
                  return new MapEntry<>(k, map.get(k));
              })
              .peek(sorted::add);
        }
        for(var entry: map.entrySet()) {
            Some(entry)
              .filter(e -> !found.contains(e.getKey()))
              .peek(sorted::add);
        }
        return sorted;
    }

    static String toString(char start, char end, Iterator iterator) {
        var builder = new StringBuilder();
        var hasNext = iterator.hasNext();
        builder.append(start);
        while (hasNext) {
            builder.append(iterator.next());
            if (hasNext = iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append(end).toString();
    }

    static <O> String toString (char start, char end, Iterator<O> it, BiConsumer<StringBuilder, O> consumer) {
        var builder = new StringBuilder();
        var hasNext = it.hasNext();
        var current = hasNext ? it.next() : null;
        builder.append(start);
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

    static <O, T extends Collection<O>> T fill(T collection, O[] values) {
        Collections.addAll(collection, values);
        return collection;
    }

    static <O extends Collection<Integer>> O fill(O collection, int[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Double>> O fill(O collection, double[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Float>> O fill(O collection, float[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Long>> O fill(O collection, long[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Short>> O fill(O collection, short[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Byte>> O fill(O collection, byte[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Boolean>> O fill(O collection, boolean[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    static <O extends Collection<Character>> O fill(O collection, char[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }
}
