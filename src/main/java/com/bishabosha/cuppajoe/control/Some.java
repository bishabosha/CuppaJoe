package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import com.bishabosha.cuppajoe.tuples.Apply1;
import com.bishabosha.cuppajoe.tuples.Product1;
import com.bishabosha.cuppajoe.tuples.Unapply1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Tuple;

public final class Some<O> implements Option<O>, Unapply1<O> {

    private O value;

    private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Some.class);

    @NotNull
    public static Pattern $Some(Pattern pattern) {
        return PATTERN.apply(pattern);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Some<O> of(@Nullable O value) {
        return new Some<>(value);
    }

    private Some(O value) {
        this.value = value;
    }

    static <O> Apply1<O, Some<O>> Applied() {
        return Func1.<O, Some<O>>of(Some::of).applied();
    }

    @NotNull
    @Override
    public Product1<O> unapply() {
        return Tuple(get());
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
        return this == obj || Option.of(obj)
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
}
