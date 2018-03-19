/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product4;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc4<A, B, C, D, R> {

    @Contract(pure = true)
    static <W,X,Y,Z,R> CheckedFunc4<W,X,Y,Z,R> of(CheckedFunc4<W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <W,X,Y,Z,R> CheckedFunc4<W,X,Y,Z,R> narrow(CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Option<R>> lift(CheckedFunc4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (w, x, y, z) -> Try.<R>of(() -> func.apply(w, x, y, z)).lift();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4());
    }

    R apply(A a, B b, C c, D d) throws Throwable;

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
