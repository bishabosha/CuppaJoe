/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product0;
import org.jetbrains.annotations.Contract;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface CheckedFunc0<R> extends Callable<R> {

    default R apply() throws Exception {
        return call();
    }

    @Contract(pure = true)
    static <R> CheckedFunc0<R> of(Callable<? extends R> reference) {
        return reference::call;
    }

    @Contract(pure = true)
    static <R> CheckedFunc0<R> narrow(Callable<? extends R> func) {
        return func::call;
    }

    @Contract(pure = true)
    static <R> Func0<Try<R>> lift(Callable<? extends R> func) {
        return () -> Try.of(func::call);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product0, R> tupled() {
        return t -> call();
    }
}
