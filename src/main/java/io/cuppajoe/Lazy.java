/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe;

import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;
import io.cuppajoe.tuples.Product1;
import io.cuppajoe.tuples.Unapply1;

import java.util.Objects;
import java.util.function.Supplier;

import static io.cuppajoe.API.Tuple;

public class Lazy<E> implements Supplier<E>, Unapply1<E> {

    public static Pattern $Lazy(Pattern pattern) {
        return PatternFactory.gen1(Lazy.class, pattern);
    }

    private boolean isComputed = false;
    private E value = null;
    private Supplier<E> getter;

    static <R> Lazy<R> of(Supplier<R> getter) {
        return new Lazy<>(getter);
    }

    private Lazy(Supplier<E> getter) {
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
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Lazy && Objects.equals(get(), ((Lazy) obj).get());
    }

    @Override
    public Product1<E> unapply() {
        return Tuple(get());
    }
}
