/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.util;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
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

    public static int hash(@NonNull Iterator<?> it) {
        Objects.requireNonNull(it, "it");
        var hash = 1;
        while (it.hasNext()) {
            var element = it.next();
            hash = 29 * hash + (element != null ? element.hashCode() : 0);
        }
        return hash;
    }

    public static <E> Iterator<E> singletonSupplier(@NonNull Supplier<? extends E> supplier) {
        Objects.requireNonNull(supplier, "supplier");
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
    public static <E> Iterator<E> ofSuppliers(@NonNull Supplier<E>... suppliers) {
        Objects.requireNonNull(suppliers, "suppliers");
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
    public static <E> Iterator<E> concat(@NonNull Iterable<E>... iterables) {
        Objects.requireNonNull(iterables, "iterables");
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

    public static <R> Iterator<R> iterate(R identity, @NonNull UnaryOperator<R> accumulator) {
        return iterate(identity, x -> false, accumulator);
    }

    public static <R> Iterator<R> iterate(R identity,
                                          @NonNull Predicate<R> terminatingCondition,
                                          @NonNull UnaryOperator<R> accumulator) {
        Objects.requireNonNull(terminatingCondition, "terminatingCondition");
        Objects.requireNonNull(accumulator, "accumulator");
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
        Objects.requireNonNull(optional, "optional");
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

    public static <E> boolean equals(@NonNull Iterator<E> base, @NonNull Iterator it) {
        Objects.requireNonNull(base, "base");
        Objects.requireNonNull(it, "it");
        while (base.hasNext()) {
            var term = base.next();
            if (!it.hasNext() || !Objects.equals(term, it.next())) {
                return false;
            }
        }
        return !it.hasNext();
    }

    public static boolean equalElements(@NonNull Collection a, @NonNull Collection b) {
        Objects.requireNonNull(a, "a");
        Objects.requireNonNull(b, "b");
        return a.size() == b.size() && a.containsAll(b) && b.containsAll(a);
    }

    public static <R> boolean equalElements(@NonNull Collection a, R... values) {
        Objects.requireNonNull(a, "a");
        return equalElements(a, List.of(values));
    }

    public static <R> Iterator<R> of(@NonNull R... values) {
        return array(values);
    }

    public static <R> Iterator<R> array(@NonNull R[] values) {
        Objects.requireNonNull(values, "values");
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

    public static <R> Iterator<R> castArray(@NonNull Object[] values) {
        Objects.requireNonNull(values, "values");
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

    public static <R> Iterator<R> cycle(@NonNull R... values) {
        return cycle(() -> of(values));
    }

    public static <R> Iterator<R> cycle(@NonNull Iterable<R> source) {
        Objects.requireNonNull(source, "source");
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

    public static String toString(char start, char end, @NonNull Iterator iterator) {
        Objects.requireNonNull(iterator, "iterator");
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

    public static <O> String toString(char start, char end, @NonNull Iterator<O> it, @NonNull BiConsumer<StringBuilder, O> consumer) {
        Objects.requireNonNull(it, "it");
        Objects.requireNonNull(consumer, "consumer");
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
