/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */
package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple4;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func4<A, B, C, D, R> {

    @Contract(pure = true)
    static <W,X,Y,Z,R> Func4<W,X,Y,Z,R> of(Func4<W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <W,X,Y,Z,R> Func4<W,X,Y,Z,R> narrow(Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <W, X, Y, Z, R> Func4<W, X, Y, Z, Try<R>> lift(Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return CheckedFunc4.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, R>>>> curried() {
        return w -> x -> y -> z -> apply(w, x, y, z);
    }

    @Contract(pure = true)
    default Func1<Tuple4<A, B, C, D>, R> tupled() {
        return x -> apply(x.$1, x.$2, x.$3, x.$4);
    }

    default <U> Func4<A, B, C, D, U> andThen(Function<? super R, ? extends U> next) {
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
