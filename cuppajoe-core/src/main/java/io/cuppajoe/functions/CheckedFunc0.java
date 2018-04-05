/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface CheckedFunc0<R> extends Callable<R> {

    default R apply() throws Exception {
        return call();
    }

    static <R> CheckedFunc0<R> of(Callable<? extends R> reference) {
        return reference::call;
    }

    static <R> CheckedFunc0<R> narrow(Callable<? extends R> func) {
        return func::call;
    }

    static <R> Func0<Try<R>> lift(Callable<? extends R> func) {
        return () -> Try.of(func::call);
    }

    default CheckedFunc1<Unit, R> tupled() {
        return t -> call();
    }
}
