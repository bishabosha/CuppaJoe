/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc1<A, R> {

    static <X, R> CheckedFunc1<X, R> of(@NonNull CheckedFunc1<X, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, R> CheckedFunc1<X, R> narrow(@NonNull CheckedFunc1<? super X, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, R> Func1<X, Value<R>> lift(@NonNull CheckedFunc1<? super X, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return x -> LiftOps.liftFunction(() -> func.apply(x));
    }

    R apply(A a) throws Exception;
}
