/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface CheckedFunc0<R> extends Callable<R> {

    default R apply() throws Exception {
        return call();
    }

    static <R> CheckedFunc0<R> of(@NonNull Callable<? extends R> reference) {
        Objects.requireNonNull(reference);
        return reference::call;
    }

    static <R> CheckedFunc0<R> narrow(@NonNull Callable<? extends R> func) {
        Objects.requireNonNull(func);
        return func::call;
    }

    static <R> Func0<Try<R>> lift(@NonNull Callable<? extends R> func) {
        Objects.requireNonNull(func);
        return () -> Try.of(func::call);
    }

    default CheckedFunc1<Unit, R> tupled() {
        return t -> call();
    }
}
