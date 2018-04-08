/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func4<A, B, C, D, R> {

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, R> of(@NonNull Func4<W, X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, R> narrow(@NonNull Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Value<R>> lift(@NonNull Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc4.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    default <U> Func4<A, B, C, D, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next, "next");
        return (s, t, u, v) -> next.apply(apply(s, t, u, v));
    }

    default Func4<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, R> lazyInput() {
        return (a, b, c, d) -> apply(a.get(), b.get(), c.get(), d.get());
    }

    R apply(A a, B b, C c, D d);

    default Func3<B, C, D, R> apply(A a) {
        return (b, c, d) -> apply(a, b, c, d);
    }

    default Func2<C, D, R> apply(A a, B b) {
        return (c, d) -> apply(a, b, c, d);
    }

    default Func1<D, R> apply(A a, B b, C c) {
        return d -> apply(a, b, c, d);
    }
}
