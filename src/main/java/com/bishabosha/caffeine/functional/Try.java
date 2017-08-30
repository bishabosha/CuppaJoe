/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.Objects;
import java.util.function.*;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Try<E> implements Supplier<E> {

    static Pattern Try(Pattern pattern) {
        return patternFor(Try.class).conditionalAtomic(Try::isPresent, Try::get, pattern);
    }

    private static Try<?> FAIL = new Try(null) {
        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public Object get() {
            throw new UnsupportedOperationException("No value is present");
        }

        @Override
        public String toString() {
            return "Error(no value is present)";
        }
    };

    private boolean didFail = false;

    private boolean isComputed = false;

    private E value = null;

    private Throwable error = null;

    private Supplier<E> getter;

    public static <O> Try<O> fail() {
        return (Try<O>) FAIL;
    }

    public static <T> Try<T> of(Supplier<T> getter) {
        return new Try<>(getter);
    }

    private Try(Supplier<E> getter) {
        this.getter = getter;
    }

    /**
     * If not computed, will compute the result and store any exceptions or errors given,
     * also memoization the result;
     * @return
     */
    public boolean isPresent() {
        if (!isComputed) {
            try {
                value = Objects.requireNonNull(getter.get());
            } catch (Throwable err) {
                didFail = true;
                error = err;
            }
            isComputed = true;
        }
        return !didFail;
    }

    /**
     * Lazily evaluates the expression, with memoization; throwing any errors encountered
     * @throws RuntimeException if computation failed.
     * @return the result of computation if successful
     */
    @Override
    public E get() {
        if (isPresent()) {
            return value;
        }
        throw new RuntimeException(error);
    }

    /**
     * Lazily evaluates the expression, with memoization; throwing any errors encountered
     * @throws RuntimeException if computation failed.
     * @return the result of computation if successful
     */
    public E getOrThrowMessage() {
        if (isPresent()) {
            return value;
        }
        throw new RuntimeException(error.getMessage());
    }

    @Override
    public String toString() {
        return isPresent() ? "Success("+value+")" : "Error("+error.getMessage()+")";
    }

    /**
     * Lazily evaluates the expression, with memoization; returns the result if
     * no errors encountered, otherwise the alternative given.
     * @throws RuntimeException, The alternative is not guarded against error;
     * @param alternative The alternate expression to evaluate if the original has failed, unchecked
     * @return the result of computation if successful, else the alternative given
     */
    public E orElseGet(Supplier<E> alternative) {
        return isPresent() ? value : alternative.get();
    }

    public E orElse(E alternative) {
        return isPresent() ? value : alternative;
    }

    public Try ifFail(Supplier toTry) {
        return !isPresent() ? new Try<>(toTry) : this;
    }

    public void ifPresent(Consumer<E> consumer) {
        if (isPresent()) {
            consumer.accept(value);
        }
    }

    public <O> Try<O> map(Function<E, O> mapper) {
        if (isPresent()) {
            return new Try<>(() -> mapper.apply(value));
        }
        return fail();
    }

    public <O> Try<O> flatMap(Function<E, Try<O>> mapper) {
        return isPresent() ? Objects.requireNonNull(mapper.apply(value)) : fail();
    }

    public Try<E> filter(Predicate<E> filter) {
        return (isPresent() && filter.test(value)) ? this : fail();
    }

    public <O> Option<O> mapOrElse(Function<E, Option<O>> mapper, Supplier<Option<O>> orElse) {
        return isPresent() ? mapper.apply(value) : orElse.get();
    }

    public <O> Option<O> flatMapOption(Function<E, Option<O>> mapper) {
        return isPresent() ? mapper.apply(value) : Option.nothing();
    }

    public <O> Option<O> mapOption(Function<E, O> mapper) {
        return isPresent() ? Option.ofUnknown(mapper.apply(value)) : Option.nothing();
    }

    public Option<E> mapOption() {
        return isPresent() ? Option.ofUnknown(value) : Option.nothing();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * @param obj The object to compare
     * @return True if obj is a Try and both computations failed, or if both completed and their values are the same.
     * False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Try)) {
            return false;
        }
        final Try<?> theTry = (Try<?>) obj;
        final boolean isPresent = isPresent();
        final boolean otherIsPresent = theTry.isPresent();
        if (isPresent ^ otherIsPresent) {
            return false;
        }
        if (!isPresent && !otherIsPresent) {
            return true;
        }
        return Objects.equals(theTry.value, value);
    }
}
