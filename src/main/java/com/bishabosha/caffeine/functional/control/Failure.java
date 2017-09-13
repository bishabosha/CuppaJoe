package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.patterns.Pattern;

import java.util.Objects;

import static com.bishabosha.caffeine.functional.API.Nothing;

public class Failure<E> implements Try<E> {

    private final Throwable error;

    static Pattern Failure() {
        return x -> x instanceof Failure ? Pattern.PASS : Pattern.FAIL;
    }

    static <T> Failure<T> of(Throwable error) {
        return new Failure<>(error);
    }

    private Failure(Throwable error) {
        this.error = error;
    }

    @Override
    public E getOrThrow() throws Throwable {
        throw error;
    }

    @Override
    public Option<E> get() {
        return Nothing();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public int hashCode() {
        return error.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Failure)) {
            return false;
        }
        final Failure<?> other = (Failure<?>) obj;
        return Objects.equals(other.error, error);
    }

    @Override
    public String toString() {
        return "Failure("+error.getMessage()+")";
    }
}
