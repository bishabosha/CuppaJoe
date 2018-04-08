/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.higher.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func6<A, B, C, D, E, F, R> {

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, R> of(@NonNull Func6<U, V, W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, R> narrow(@NonNull Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Value<R>> lift(@NonNull Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc6.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    default <U> Func6<A, B, C, D, E, F, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next, "next");
        return (s, t, u, v, w, x) -> next.apply(apply(s, t, u, v, w, x));
    }

    default Func6<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, Supplier<F>, R> lazyInput() {
        return (a, b, c, d, e, f) -> apply(a.get(), b.get(), c.get(), d.get(), e.get(), f.get());
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
