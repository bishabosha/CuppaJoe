/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.functions.CheckedFunc0;
import com.bishabosha.caffeine.functional.functions.Func0;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

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
