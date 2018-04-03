package io.cuppajoe.functions;

import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Allows simulation of tail call operations by lifting the call site
 * Implemented following "Lazy Java" presentation by Mario Fusco at Devoxx Belgium November 2017
 * @param <E> the type of the return value
 */
public abstract class TailCall<E> implements Func0<E> {

    public static <U> TailCall<U> call(Supplier<TailCall<U>> call) {
        return new Call<>(call);
    }

    public static <U> TailCall<U> yield(U result) {
        return new Yield<>(result);
    }

    private TailCall() {
    }

    @Override
    public final E get() {
        return Stream.iterate(this, TailCall::next)
            .filter(TailCall::isComplete)
            .findFirst()
            .get()
            .result();
    }

    protected abstract TailCall<E> next();

    protected boolean isComplete() {
        return false;
    }

    protected E result() {
        throw new NoSuchElementException("The operation is not complete.");
    }

    private static final class Call<E> extends TailCall<E> {
        private final Supplier<TailCall<E>> call;

        private Call(Supplier<TailCall<E>> callSupplier) {
            call = callSupplier;
        }

        @Override
        public TailCall<E> next() {
            return call.get();
        }
    }

    private static final class Yield<E> extends TailCall<E> {
        private final E val;

        private Yield(E val) {
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
