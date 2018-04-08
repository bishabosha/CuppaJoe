/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

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

    static <R> Func0<Value<R>> lift(@NonNull Callable<? extends R> func) {
        Objects.requireNonNull(func, "func");
        return () -> LiftOps.liftFunction(func::call);
    }
}
