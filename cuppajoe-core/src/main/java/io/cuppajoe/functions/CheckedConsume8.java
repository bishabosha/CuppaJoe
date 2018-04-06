/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.annotation.NonNull;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple8;
import io.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume8<A, B, C, D, E, F, G, H> {

    void apply(A a, B b, C c, D d, E e, F f, G g, H h) throws Exception;

    static <S, T, U, V, W, X, Y, Z> CheckedConsume8<S, T, U, V, W, X, Y, Z> of(@NonNull CheckedConsume8<S, T, U, V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference);
    }

    static <S, T, U, V, W, X, Y, Z> CheckedConsume8<S, T, U, V, W, X, Y, Z> narrow(@NonNull CheckedConsume8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return func::apply;
    }

    static <S, T, U, V, W, X, Y, Z> Func8<S, T, U, V, W, X, Y, Z, Try<Unit>> lift(@NonNull CheckedConsume8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func);
        return (s, t, u, v, w, x, y, z) -> Try.of(() -> {
            func.apply(s, t, u, v, w, x, y, z);
            return Unit.INSTANCE;
        });
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, CheckedConsume1<H>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    default CheckedConsume1<Tuple8<A, B, C, D, E, F, G, H>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7, x.$8);
    }

    default CheckedConsume8<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>, Supplier<H>> lazyInput() {
        return (a, b, c, d, e, f, g, h) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get());
    }

    default CheckedConsume1<H> apply(A a, B b, C c, D d, E e, F f, G g) {
        return h -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume2<G, H> apply(A a, B b, C c, D d, E e, F f) {
        return (g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume3<F, G, H> apply(A a, B b, C c, D d, E e) {
        return (f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume4<E, F, G, H> apply(A a, B b, C c, D d) {
        return (e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume5<D, E, F, G, H> apply(A a, B b, C c) {
        return (d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume6<C, D, E, F, G, H> apply(A a, B b) {
        return (c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }

    default CheckedConsume7<B, C, D, E, F, G, H> apply(A a) {
        return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
    }
}
