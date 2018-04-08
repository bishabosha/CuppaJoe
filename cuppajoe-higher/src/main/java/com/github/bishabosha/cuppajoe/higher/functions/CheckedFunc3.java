/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc3<A, B, C, R> {

    static <X, Y, Z, R> CheckedFunc3<X, Y, Z, R> of(@NonNull CheckedFunc3<X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y, Z, R> CheckedFunc3<X, Y, Z, R> narrow(@NonNull CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y, Z, R> Func3<X, Y, Z, Value<R>> lift(@NonNull CheckedFunc3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (x, y, z) -> LiftOps.liftFunction(() -> func.apply(x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    R apply(A a, B b, C c) throws Exception;

    default CheckedFunc2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default CheckedFunc1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
