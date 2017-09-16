package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Nothing<E> implements Option<E> {

    private static final Nothing<?> NOTHING = new Nothing<>();

    @NotNull
    @Contract(pure = true)
    public static final Pattern Nothing() {
        return x -> NOTHING.equals(x) ? Pattern.PASS : Pattern.FAIL;
    }

    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <O> Nothing<O> getInstance() {
        return (Nothing<O>) NOTHING;
    }

    private Nothing() {
    }

    @Contract(pure = true)
    @Override
    public boolean isSome() {
        return false;
    }

    @Contract(" -> fail")
    @Override
    public E get() {
        throw new IllegalStateException("There is nothing present.");
    }

    @Override
    public String toString() {
        return "Nothing";
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Iterables.<E>empty().iterator();
    }
}
