/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.Objects;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.Pattern.$x;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Lazy<E> implements Supplier<E> {

    public static Pattern Lazy(Pattern pattern) {
        return patternFor(Lazy.class).atomic(pattern, Lazy::get);
    }

    private boolean isComputed = false;
    private E value = null;
    private Supplier<E> getter;

    static <R> Lazy<R> Get(Supplier<R> getter) {
        return new Lazy<>(getter);
    }

    Lazy(Supplier<E> getter) {
        this.getter = getter;
    }

    @Override
    public E get() {
        if (isComputed) {
            return value;
        }
        isComputed = true;
        return value = getter.get();
    }

    @Override
    public String toString() {
        return "Lazy("+get()+")";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ? true : Option.ofUnknown(obj)
                                          .cast(Lazy.class)
                                          .map(l -> Objects.equals(l.get(), get()))
                                          .orElse(false);
    }
}
