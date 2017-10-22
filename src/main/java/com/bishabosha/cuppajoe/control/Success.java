package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;

public class Success<E> implements Try<E> {

    private final E value;

    static Pattern $Success(Pattern pattern) {
        return patternFor(Success.class).test1(pattern, x -> x.value);
    }

    @NotNull
    static <T> Success<T> of(T value) {
        return new Success<>(value);
    }

    private Success(E value) {
        this.value = value;
    }

    @Override
    public E getOrThrow() throws Throwable {
        return value;
    }

    @Override
    public Option<E> get() {
        return Option(value);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Success)) {
            return false;
        }
        final Success<?> other = (Success<?>) obj;
        return Objects.equals(other.value, value);
    }

    @Override
    public String toString() {
        return "Success("+value+")";
    }
}
