/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.functions.CheckedFunc0;
import com.bishabosha.caffeine.functional.functions.Func0;

public interface Try<E> {

    static <T> Try<T> of(Func0<T> getter) {
        return of(CheckedFunc0.of(getter::apply));
    }

    static <T> Try<T> of(CheckedFunc0<T> getter) {
        try {
            final T value = getter.apply();
            return Success.of(value);
        } catch (Throwable e) {
            return Failure.of(e);
        }
    }

    @SuppressWarnings("unchecked")
    static  <O> Try<O> narrow(Try<? extends O> toNarrow) {
        return (Try<O>) toNarrow;
    }

    boolean isPresent();

    E getOrThrow() throws Throwable;

    Option<E> get();
}
