package com.github.bishabosha.cuppajoe.higher.functions;

import java.util.NoSuchElementException;

/**
 * Allows simulation of tail call operations by lifting the call site
 * Implemented following "Lazy Java" presentation by Mario Fusco at Devoxx Belgium November 2017
 *
 * @param <E> the type of the return value
 */
@FunctionalInterface
public interface TailCall<E> {

    static <U> TailCall<U> yield(U result) {
        return new Yield<>(result);
    }

    TailCall<E> next();

    default E apply() {
        var call = this;
        while (call.isEmpty()) {
            call = call.next();
        }
        return call.apply();
    }

    default boolean isEmpty() {
        return true;
    }

    final class Yield<E> implements TailCall<E> {
        private final E val;

        private Yield(E val) {
            this.val = val;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public TailCall<E> next() {
            throw new NoSuchElementException("This is a Terminal operation.");
        }

        @Override
        public E apply() {
            return val;
        }
    }
}
