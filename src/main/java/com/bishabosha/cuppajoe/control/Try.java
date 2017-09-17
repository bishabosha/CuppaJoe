/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.functions.CheckedFunc0;
import org.jetbrains.annotations.Contract;

public interface Try<E> {

    static <T> Try<T> of(CheckedFunc0<? extends T> getter) {
        try {
            return Success.of(getter.apply());
        } catch (Throwable e) {
            return Failure.of(e);
        }
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static  <O> Try<O> narrow(Try<? extends O> toNarrow) {
        return (Try<O>) toNarrow;
    }

    boolean isPresent();

    E getOrThrow() throws Throwable;

    Option<E> get();
}
