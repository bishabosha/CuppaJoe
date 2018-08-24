/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.control;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply1;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<E> implements Supplier<E>, Unapply1<E> {

    private static final VarHandle VALUE;

    private boolean isComputed;
    private volatile E value;
    private final Supplier<E> factory;

    static {
        var lookup = MethodHandles.lookup();
        try {
            VALUE = lookup.findVarHandle(Lazy.class, "value", Object.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    public static <R> Lazy<R> of(@NonNull Supplier<R> factory) {
        Objects.requireNonNull(factory, "factory");
        return new Lazy<>(factory);
    }

    private Lazy(Supplier<E> factory) {
        this.factory = factory;
        value = null;
        isComputed = false;
    }

    public final E memo() {
        while (!isComputed) {
            isComputed = VALUE.compareAndSet(this, null, factory.get());
        }
        return value;
    }

    @Override
    public E get() {
        return memo();
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
