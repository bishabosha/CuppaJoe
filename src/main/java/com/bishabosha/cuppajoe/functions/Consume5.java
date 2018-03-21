/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product5;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface Consume5<A, B, C, D, E> {

    void apply(A a, B b, C c, D d, E e);

    @NotNull
    @Contract(pure = true)
    static <V, W, X, Y, Z> Consume5<V, W, X, Y, Z> of(Consume5<V, W, X, Y, Z> reference) {
        return reference;
    }

    @NotNull
    @Contract(pure = true)
    static <V, W, X, Y, Z> Consume5<V, W, X, Y, Z> narrow(Consume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <V, W, X, Y, Z> Func5<V, W, X, Y, Z, Try<Void>> lift(Consume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        return CheckedConsume5.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Consume1<E>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    @Contract(pure = true)
    default Consume1<Product5<A, B, C, D, E>> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5());
    }

    default Consume5<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>> lazyInput() {
        return (a, b, c, d, e) -> apply(a.get(), b.get(), c.get(), d.get(), e.get());
    }

    default Consume1<E> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }

    default Consume2<D, E> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default Consume3<C, D, E> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default Consume4<B, C, D, E> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }
}
