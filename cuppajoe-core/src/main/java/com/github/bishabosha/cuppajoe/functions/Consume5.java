/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple5;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume5<A, B, C, D, E> {

    void apply(A a, B b, C c, D d, E e);

    static <V, W, X, Y, Z> Consume5<V, W, X, Y, Z> of(@NonNull Consume5<V, W, X, Y, Z> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <V, W, X, Y, Z> Consume5<V, W, X, Y, Z> narrow(@NonNull Consume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <V, W, X, Y, Z> Func5<V, W, X, Y, Z, Try<Unit>> lift(@NonNull Consume5<? super V, ? super W, ? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return CheckedConsume5.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Consume1<E>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    default Consume1<Tuple5<A, B, C, D, E>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5);
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
