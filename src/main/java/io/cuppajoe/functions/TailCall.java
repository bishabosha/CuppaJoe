package io.cuppajoe.functions;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Allows simulation of tail call operations by lifting the call site
 * Implemented following "Lazy Java" presentation by Mario Fusco at Devoxx Belgium November 2017
 * @param <E> the type of the return value
 */
@FunctionalInterface
public interface TailCall<E> {

    static <U> TailCall<U> rec(TailCall<U> call) {
        return call;
    }

    static <U> TailCall<U> yield(U result) {
        return new Terminal<>(result);
    }

    TailCall<E> next();

    default boolean isComplete() {
        return false;
    }

    default E result() {
        throw new NoSuchElementException("The operation is not complete.");
    }

    default E invoke() {
        return Stream.iterate(this, TailCall::next)
            .filter(TailCall::isComplete)
            .findFirst()
            .get()
            .result();
    }

    class Terminal<E> implements TailCall<E> {
        private final E val;

        private Terminal(E val) {
            this.val = val;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public TailCall<E> next() {
            throw new NoSuchElementException("This is a Terminal operation.");
        }

        @Override
        public E result() {
            return val;
        }
    }
}
