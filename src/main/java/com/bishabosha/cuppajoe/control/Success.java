package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import com.bishabosha.cuppajoe.tuples.Product1;
import com.bishabosha.cuppajoe.tuples.Unapply1;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;
import static com.bishabosha.cuppajoe.API.Tuple;

public class Success<E> implements Try<E>, Unapply1<E>{

    private final E value;

    private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Success.class);

    static Pattern $Success(Pattern pattern) {
        return PATTERN.apply(pattern);
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

    @Override
    public Product1<E> unapply() {
        return Tuple(value);
    }
}
