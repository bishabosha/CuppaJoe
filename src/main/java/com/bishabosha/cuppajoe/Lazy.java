/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import com.bishabosha.cuppajoe.tuples.Product1;
import com.bishabosha.cuppajoe.tuples.Unapply1;

import java.util.Objects;
import java.util.function.Supplier;

import static com.bishabosha.cuppajoe.API.Option;
import static com.bishabosha.cuppajoe.API.Tuple;

public class Lazy<E> implements Supplier<E>, Unapply1<E> {

    private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Lazy.class);

    public static Pattern Lazy(Pattern pattern) {
        return PATTERN.apply(pattern);
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
        return this == obj || Option(obj)
                                  .cast(Lazy.class)
                                  .map(l -> Objects.equals(l.get(), get()))
                                  .orElse(false);
    }

    @Override
    public Product1<E> unapply() {
        return Tuple(get());
    }
}
