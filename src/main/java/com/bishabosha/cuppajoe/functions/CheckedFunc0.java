/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product0;
import org.jetbrains.annotations.Contract;

public interface CheckedFunc0<R> {

    @Contract(pure = true)
    static <R> CheckedFunc0<R> of(CheckedFunc0<R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <R> CheckedFunc0<R> narrow(CheckedFunc0<? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <R> Func0<Option<R>> lift(CheckedFunc0<? extends R> func) {
        return Try.<R>of(func::apply)::lift;
    }

    @Contract(pure = true)
    default CheckedFunc1<Product0, R> tupled() {
        return t -> apply();
    }

    R apply() throws Throwable;
}
