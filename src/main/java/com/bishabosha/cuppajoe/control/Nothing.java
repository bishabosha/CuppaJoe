package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Nothing<E> implements Option<E> {

    private static final Nothing<?> NOTHING = new Nothing<>();

    @NotNull
    @Contract(pure = true)
    public static final Pattern $Nothing() {
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
    public boolean isEmpty() {
        return true;
    }

    @Contract(" -> fail")
    @Override
    public E get() {
        throw new NoSuchElementException("There is nothing present.");
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Nothing";
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Iterables.empty();
    }
}
