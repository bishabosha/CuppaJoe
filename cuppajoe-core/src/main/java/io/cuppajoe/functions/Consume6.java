/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple6;
import io.cuppajoe.tuples.Unit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface Consume6<A, B, C, D, E, F> {

    void apply(A a, B b, C c, D d, E e, F f);

    @NotNull
    @Contract(pure = true)
    static <U, V, W, X, Y, Z> Consume6<U, V, W, X, Y, Z> of(Consume6<U, V, W, X, Y, Z> reference) {
        return reference;
    }

    @NotNull
    @Contract(pure = true)
    static <U, V, W, X, Y, Z> Consume6<U, V, W, X, Y, Z> narrow(Consume6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <U, V, W, X, Y, Z> Func6<U, V, W, X, Y, Z, Try<Unit>> lift(Consume6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return CheckedConsume6.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Consume1<F>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    @Contract(pure = true)
    default Consume1<Tuple6<A, B, C, D, E, F>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6);
    }

    default Consume6<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>> lazyInput() {
        return (a, b, c, d, e, f) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get());
    }

    default Consume1<F> apply(A a, B b, C c, D d, E e) {
        return f -> apply(a, b, c, d, e, f);
    }

    default Consume2<E, F> apply(A a, B b, C c, D d) {
        return (e, f) -> apply(a, b, c, d, e, f);
    }

    default Consume3<D, E, F> apply(A a, B b, C c) {
        return (d, e, f) -> apply(a, b, c, d, e, f);
    }

    default Consume4<C, D, E, F> apply(A a, B b) {
        return (c, d, e, f) -> apply(a, b, c, d, e, f);
    }

    default Consume5<B, C, D, E, F> apply(A a) {
        return (b, c, d, e, f) -> apply(a, b, c, d, e, f);
    }
}
