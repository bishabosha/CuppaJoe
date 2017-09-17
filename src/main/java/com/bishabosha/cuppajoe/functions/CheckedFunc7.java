/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product7;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CheckedFunc7<A, B, C, D, E, F, G, R> {

    @Contract(pure = true)
    static <T,U,V,W,X,Y,Z,R> CheckedFunc7<T,U,V,W,X,Y,Z,R> of(CheckedFunc7<T, U, V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <T,U,V,W,X,Y,Z,R> CheckedFunc7<T,U,V,W,X,Y,Z,R> narrow(CheckedFunc7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <T, U, V, W, X, Y, Z, R> Func7<T, U, V, W, X, Y, Z, Option<R>> lift(CheckedFunc7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (t, u, v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(t, u, v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    default CheckedFunc1<A, CheckedFunc1<B, CheckedFunc1<C, CheckedFunc1<D, CheckedFunc1<E, CheckedFunc1<F, CheckedFunc1<G, R>>>>>>> curried() {
        return t -> u -> v -> w -> x -> y -> z -> apply(t, u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default CheckedFunc1<Product7<A, B, C, D, E, F, G>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7());
    }

    R apply(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

    default CheckedFunc6<B, C, D, E, F, G, R> apply(A a) {
        return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc5<C, D, E, F, G, R> apply(A a, B b) {
        return (c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc4<D, E, F, G, R> apply(A a, B b, C c) {
        return (d, e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc3<E, F, G, R> apply(A a, B b, C c, D d) {
        return (e, f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc2<F, G, R> apply(A a, B b, C c, D d, E e) {
        return (f, g) -> apply(a, b, c, d, e, f, g);
    }

    default CheckedFunc1<G, R> apply(A a, B b, C c, D d, E e, F f) {
        return g -> apply(a, b, c, d, e, f, g);
    }
}
