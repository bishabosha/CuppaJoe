package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;

public final class Some<O> implements Option<O> {

    private O value;

    @NotNull
    public static Pattern Some(Pattern pattern) {
        return patternFor(Some.class).atomic(pattern, Some::get);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Some<O> of(@Nullable O value) {
        return new Some<>(value);
    }

    private Some(O value) {
        this.value = value;
    }

    @Override
    public boolean isSome() {
        return true;
    }

    @Contract(pure = true)
    @Override
    public O get() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ? true : Option.of(obj)
            .cast(Some.class)
            .map(o -> Objects.equals(o.get(), get()))
            .orElse(false);
    }

    @Override
    public String toString() {
        return "Some(" + get() + ")";
    }

    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>() {
            boolean unwrapped = false;

            @Override
            public boolean hasNext() {
                return !unwrapped;
            }

            @Override
            public O next() {
                unwrapped = true;
                return get();
            }
        };
    }
}
