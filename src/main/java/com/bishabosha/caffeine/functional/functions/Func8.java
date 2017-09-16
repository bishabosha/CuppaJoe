/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Apply8;
import com.bishabosha.caffeine.functional.tuples.Product8;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Func8<A, B, C, D, E, F, G, H, R> {

    @Contract(pure = true)
    @NotNull
    static <S,T,U,V,W,X,Y,Z,R> Func8<S,T,U,V,W,X,Y,Z,R> of(Func8<S,T,U,V,W,X,Y,Z,R> func) {
        return func;
    }

    @Contract(pure = true)
    @NotNull
    static <S,T,U,V,W,X,Y,Z,R> Func8<S,T,U,V,W,X,Y,Z,R> narrow(Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, Option<R>> lift(Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return (s, t, u, v, w, x, y, z) -> Try.<R>narrow(Try.of(() -> func.apply(s, t, u, v, w, x, y, z))).get();
    }

    @Contract(pure = true)
    @NotNull
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, Func1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    @Contract(pure = true)
    @NotNull
    default Func1<Product8<A, B, C, D, E, F, G, H>, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7(), x.$8());
    }

    @Contract(pure = true)
    default Apply8<A, B, C, D, E, F, G, H, R> applied() {
        return x -> tupled().apply(x.unapply());
    }

    R apply(A a, B b, C c, D d, E e, F f, G g, H h);

    default Func7<B, C, D, E, F, G, H, R> apply(A a) {
        return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func6<C, D, E, F, G, H, R> apply(A a, B b) {
        return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func5<D, E, F, G, H, R> apply(A a, B b, C c) {
        return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func4<E, F, G, H, R> apply(A a, B b, C c, D d) {
        return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func3<F, G, H, R> apply(A a, B b, C c, D d, E e) {
        return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func2<G, H, R> apply(A a, B b, C c, D d, E e, F f) {
        return (g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Func1<H, R> apply(A a, B b, C c, D d, E e, F f, G g) {
        return h -> apply(a, b, c, d, e, f, g, h);
    }
}
