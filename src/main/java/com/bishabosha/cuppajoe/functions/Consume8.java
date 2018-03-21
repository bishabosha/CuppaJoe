/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product8;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface Consume8<A, B, C, D, E, F, G, H> {

    void apply(A a, B b, C c, D d, E e, F f, G g, H h);

    @NotNull
    @Contract(pure = true)
    static <S, T, U, V, W, X, Y, Z> Consume8<S, T, U, V, W, X, Y, Z> of(Consume8<S, T, U, V, W, X, Y, Z> reference) {
        return reference;
    }

    @NotNull
    @Contract(pure = true)
    static <S, T, U, V, W, X, Y, Z> Consume8<S, T, U, V, W, X, Y, Z> narrow(Consume8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <S, T, U, V, W, X, Y, Z> Func8<S, T, U, V, W, X, Y, Z, Try<Void>> lift(Consume8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return CheckedConsume8.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, Consume1<H>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Consume1<Product8<A, B, C, D, E, F, G, H>> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5(), x.$6(), x.$7(), x.$8());
    }

    default Consume8<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>, Supplier<H>> lazyInput() {
        return (a, b, c, d, e, f, g, h) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get());
    }

    default Consume1<H> apply(A a, B b, C c, D d, E e, F f, G g) {
        return h -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume2<G, H> apply(A a, B b, C c, D d, E e, F f) {
        return (g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume3<F, G, H> apply(A a, B b, C c, D d, E e) {
        return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume4<E, F, G, H> apply(A a, B b, C c, D d) {
        return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume5<D, E, F, G, H> apply(A a, B b, C c) {
        return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume6<C, D, E, F, G, H> apply(A a, B b) {
        return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default Consume7<B, C, D, E, F, G, H> apply(A a) {
        return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }
}
