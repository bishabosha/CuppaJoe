/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product5;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc5<A, B, C, D, E, R> {

    @Contract(pure = true)
    static <V,W,X,Y,Z,R> CheckedFunc5<V,W,X,Y,Z,R> of(CheckedFunc5<V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <V,W,X,Y,Z,R> CheckedFunc5<V,W,X,Y,Z,R> narrow(CheckedFunc5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, Option<R>> lift(CheckedFunc5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product5<A, B, C, D, E>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5());
    }

    R apply(A a, B b, C c, D d, E e) throws Throwable;

    default CheckedFunc4<B, C, D, E, R> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc3<C, D, E, R> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc2<D, E, R> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default CheckedFunc1<E, R> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }
}
