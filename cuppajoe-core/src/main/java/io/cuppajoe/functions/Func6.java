/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple6;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func6<A, B, C, D, E, F, R> {

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, R> of(Func6<U, V, W, X, Y, Z, R> reference) {
        return reference;
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, R> narrow(Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    static <U, V, W, X, Y, Z, R> Func6<U, V, W, X, Y, Z, Try<R>> lift(Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return CheckedFunc6.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, Func1<F, R>>>>>> curried() {
        return u -> v -> w -> x -> y -> z -> apply(u, v, w, x, y, z);
    }

    default Func1<Tuple6<A, B, C, D, E, F>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6);
    }

    default <U> Func6<A, B, C, D, E, F, U> andThen(Function<? super R, ? extends U> next) {
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
