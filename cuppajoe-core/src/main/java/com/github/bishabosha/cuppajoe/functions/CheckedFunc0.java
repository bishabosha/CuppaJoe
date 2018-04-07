/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.concurrent.Callable;

@FunctionalInterface
public interface CheckedFunc0<R> extends Callable<R> {

    default R apply() throws Exception {
        return call();
    }

    static <R> CheckedFunc0<R> of(@NonNull Callable<? extends R> reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::call;
    }

    static <R> CheckedFunc0<R> narrow(@NonNull Callable<? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::call;
    }

    static <R> Func0<Try<R>> lift(@NonNull Callable<? extends R> func) {
        Objects.requireNonNull(func, "func");
        return () -> Try.of(func::call);
    }

    default CheckedFunc1<Unit, R> tupled() {
        return t -> call();
    }
}
