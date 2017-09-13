package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.patterns.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;

public class Success<E> implements Try<E> {

    private final E value;

    static Pattern Success(Pattern pattern) {
        return patternFor(Success.class).atomic(pattern, x -> x.value);
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
        return Option.ofUnknown(value);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
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
