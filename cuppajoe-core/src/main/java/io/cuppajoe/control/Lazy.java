/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.control;

import io.cuppajoe.tuples.Unapply1;

import java.util.Objects;
import java.util.function.Supplier;

public class Lazy<E> implements Supplier<E>, Unapply1<E> {

    private boolean isComputed = false;
    private E value = null;
    private Supplier<E> getter;

    public static <R> Lazy<R> of(Supplier<R> getter) {
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
        return "Lazy(" + get() + ")";
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
    public E unapply() {
        return get();
    }
}
