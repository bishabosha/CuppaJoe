/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply6;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

public interface Func6<A, B, C, D, E, F, R> {

    @Contract(pure = true)
    static <U,V,W,X,Y,Z,R> Func6<U,V,W,X,Y,Z,R> of(Func6<U, V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <U,V,W,X,Y,Z,R> Func6<U,V,W,X,Y,Z,R> narrow(Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Option<R>> lift(Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (u, v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(u, v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Apply6<A, B, C, D, E, F, R> applied() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6());
    }

    default <U> Func6<A, B, C, D, E, F, U> andThen(Function<? super R, ? extends U> next) {
        return (s, t, u, v, w, x) -> next.apply(apply(s, t, u, v, w, x));
    }

    R apply(A a, B b, C c, D d, E e, F f);

    default Func5<B, C, D, E, F, R> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default Func4<C, D, E, F, R> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default Func3<D, E, F, R> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    default Func2<E, F, R> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    default Func1<F, R> apply(A a, B b, C c, D d, E e) {
        return f -> apply(a, b, c, d, e, f);
    }
}
