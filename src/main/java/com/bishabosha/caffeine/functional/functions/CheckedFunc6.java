/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Product6;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc6<A, B, C, D, E, F, R> {

    @Contract(pure = true)
    static <U,V,W,X,Y,Z,R> CheckedFunc6<U,V,W,X,Y,Z,R> of(CheckedFunc6<U, V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @NotNull
    @Contract(pure = true)
    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Option<R>> lift(CheckedFunc6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (u, v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(u, v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product6<A, B, C, D, E, F>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6());
    }

    R apply(A a, B b, C c, D d, E e, F f) throws Throwable;

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
