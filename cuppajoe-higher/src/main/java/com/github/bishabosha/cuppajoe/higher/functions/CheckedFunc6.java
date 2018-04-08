/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;

@FunctionalInterface
public interface CheckedFunc6<A, B, C, D, E, F, R> {

    static <U, V, W, X, Y, Z, R> CheckedFunc6<U, V, W, X, Y, Z, R> of(@NonNull CheckedFunc6<U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <T, U, V, W, X, Y, R> CheckedFunc6<T, U, V, W, X, Y, R> narrow(CheckedFunc6<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Value<R>> lift(@NonNull CheckedFunc6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return (u, v, w, x, y, z) -> LiftOps.liftFunction(() -> func.apply(u, v, w, x, y, z));
    }

    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    R apply(A a, B b, C c, D d, E e, F f) throws Exception;

    default CheckedFunc5<B, C, D, E, F, R> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc4<C, D, E, F, R> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc3<D, E, F, R> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc2<E, F, R> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    default CheckedFunc1<F, R> apply(A a, B b, C c, D d, E e) {
        return f -> apply(a, b, c, d, e, f);
    }
}
