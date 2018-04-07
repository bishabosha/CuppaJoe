/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple8;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func8<A, B, C, D, E, F, G, H, R> {

    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, R> of(@NonNull Func8<S, T, U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, R> narrow(@NonNull Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <S, T, U, V, W, X, Y, Z, R> Func8<S, T, U, V, W, X, Y, Z, Try<R>> lift(@NonNull Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc8.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, Func1<G, Func1<H, R>>>>>>>> curried() {
        return s -> t -> u -> v -> w -> x -> y -> z -> apply(s, t, u, v, w, x, y, z);
    }

    default Func1<Tuple8<A, B, C, D, E, F, G, H>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7, x.$8);
    }

    default <U> Func8<A, B, C, D, E, F, G, H, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next, "next");
        return (s, t, u, v, w, x, y, z) -> next.apply(apply(s, t, u, v, w, x, y, z));
    }

    default Func8<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, Supplier<G>, Supplier<H>, R> lazyInput() {
        return (a, b, c, d, e, f, g, h) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get(), g.get(), h.get());
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
