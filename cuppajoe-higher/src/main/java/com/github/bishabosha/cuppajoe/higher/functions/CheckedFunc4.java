/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc4<A, B, C, D, R> {

    static <W, X, Y, Z, R> CheckedFunc4<W, X, Y, Z, R> of(@NonNull CheckedFunc4<W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <W, X, Y, Z, R> CheckedFunc4<W, X, Y, Z, R> narrow(@NonNull CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Value<R>> lift(@NonNull CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (w, x, y, z) -> LiftOps.liftFunction(() -> func.apply(w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    R apply(A a, B b, C c, D d) throws Exception;

    default CheckedFunc3<B, C, D, R> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default CheckedFunc2<C, D, R> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default CheckedFunc1<D, R> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }
}
