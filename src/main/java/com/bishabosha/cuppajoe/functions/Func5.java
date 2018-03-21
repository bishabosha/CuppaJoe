/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply5;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func5<A, B, C, D, E, R> {

    @Contract(pure = true)
    static <V,W,X,Y,Z,R> Func5<V,W,X,Y,Z,R> of(Func5<V, W, X, Y, Z, R> reference) {
        return reference;
    }

    @Contract(pure = true)
    static <V,W,X,Y,Z,R> Func5<V,W,X,Y,Z,R> narrow(Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <V, W, X, Y, Z, R> Func5<V, W, X, Y, Z, Try<R>> lift(Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> func) {
        return CheckedFunc5.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, Func1<C, Func1<D, Func1<E, R>>>>> curried() {
        return v -> w -> x -> y -> z -> apply(v, w, x, y, z);
    }

    @Contract(pure = true)
    default Apply5<A, B, C, D, E, R> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3(), x.$4(), x.$5());
    }

    default <U> Func5<A, B, C, D, E, U> andThen(Function<? super R, ? extends U> next) {
        return (s, t, u, v, w) -> next.apply(apply(s, t, u, v, w));
    }

    default Func5<Supplier<A>, Supplier<B>, Supplier<C>, Supplier<D>, Supplier<E>, R> lazyInput() {
        return (a, b, c, d, e) -> apply(a.get(), b.get(), c.get(), d.get(), e.get());
    }

    R apply(A a, B b, C c, D d, E e);

    default Func4<B, C, D, E, R> apply(A a) {
        return (b, c, d, e) -> apply(a, b, c, d, e);
    }

    default Func3<C, D, E, R> apply(A a, B b) {
        return (c, d, e) -> apply(a, b, c, d, e);
    }

    default Func2<D, E, R> apply(A a, B b, C c) {
        return (d, e) -> apply(a, b, c, d, e);
    }

    default Func1<E, R> apply(A a, B b, C c, D d) {
        return e -> apply(a, b, c, d, e);
    }
}
