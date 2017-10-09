package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.functions.CheckedFunc0;
import com.bishabosha.cuppajoe.patterns.Pattern;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Nothing;

public class Failure<E> implements Try<E> {

    private final Throwable error;

    static Pattern ¥Failure() {
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
        CheckedFunc0<String> func = () -> "";
        CheckedFunc0<Object> func0 = CheckedFunc0.narrow(func);
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
