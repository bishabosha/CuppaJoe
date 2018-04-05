/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class Iterators {

    private Iterators() {
    }

    public static abstract class IdempotentIterator<E> implements Iterator<E> {
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

    public static int hash(Iterator<?> it) {
        var hash = 1;
        while (it.hasNext()) {
            var element = it.next();
            hash = 29 * hash + (element != null ? element.hashCode() : 0);
        }
        return hash;
    }

    public static <E> Iterator<E> singletonSupplier(Supplier<? extends E> supplier) {
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

    public static <E> Iterator<E> singleton(E value) {
        return singletonSupplier(() -> value);
    }

    @SafeVarargs
    public static <E> Iterator<E> ofSuppliers(Supplier<E>... suppliers) {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < suppliers.length;
            }

            @Override
            public E next() {
                return suppliers[i++].get();
            }
        };
    }

    @SafeVarargs
    public static <E> Iterator<E> concat(Iterable<E>... iterables) {
        return new IdempotentIterator<>() {
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

    public static <R> Iterator<R> iterate(R identity, UnaryOperator<R> accumulator) {
        return iterate(identity, x -> false, accumulator);
    }

    public static <R> Iterator<R> iterate(R identity,
                                          Predicate<R> terminatingCondition,
                                          UnaryOperator<R> accumulator) {
        return new IdempotentIterator<>() {
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

    public static <R> Iterator<R> fromOptional(Optional<R> optional) {
        return new Iterator<>() {

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

    public static <E> boolean equals(Iterator<E> base, Iterator it) {
        while (base.hasNext()) {
            var term = base.next();
            if (!it.hasNext() || !Objects.equals(term, it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    public static boolean equalElements(Collection a, Collection b) {
        return a.size() == b.size() && a.containsAll(b) && b.containsAll(a);
    }

    public static <R> boolean equalElements(Collection a, R... values) {
        return equalElements(a, List.of(values));
    }

    public static <R> Iterator<R> of(R... values) {
        return array(values);
    }

    public static <R> Iterator<R> array(R[] values) {
        return new Iterator<>() {

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

    public static <R> Iterator<R> castArray(Object[] values) {
        return new Iterator<>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public R next() {
                return (R) values[i++];
            }
        };
    }

    public static <R> Iterator<R> cycle(R... values) {
        return cycle(() -> of(values));
    }

    public static <R> Iterator<R> cycle(Iterable<R> source) {
        return new IdempotentIterator<>() {

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

    public static String toString(char start, char end, Iterator iterator) {
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

    public static <O> String toString(char start, char end, Iterator<O> it, BiConsumer<StringBuilder, O> consumer) {
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
}
