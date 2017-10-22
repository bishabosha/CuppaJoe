package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.tuples.Applied1;
import com.bishabosha.cuppajoe.tuples.Product1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Tuple;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;

public final class Some<O> implements Option<O>, Applied1<O, Option<O>>{

    private O value;

    @NotNull
    public static Pattern $Some(Pattern pattern) {
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

    @Contract(pure = true)
    @Override
    public boolean isEmpty() {
        return false;
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

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "$Some(" + get() + ")";
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public Iterator<O> iterator() {
        return Iterables.singleton(this::get);
    }

    @NotNull
    @Override
    public Option<O> apply(Product1<O> tuple) {
        return of(tuple.$1());
    }

    @NotNull
    @Override
    public Option<Product1<O>> unapply() {
        return of(Tuple(get()));
    }
}
