/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Product2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func2<A, B, R> extends BiFunction<A, B, R> {

    @NotNull
    @Contract(pure = true)
    static <X,Y,R> Func2<X,Y,R> of(BiFunction<X, Y, R> reference) {
        return reference::apply;
    }

    @NotNull
    @Contract(pure = true)
    static <X,Y,R> Func2<X,Y,R> narrow(BiFunction<? super X, ? super Y, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <X, Y, R> Func2<X, Y, Try<R>> lift(BiFunction<? super X, ? super Y, ? extends R> func) {
        return CheckedFunc2.lift(func::apply);
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    @Contract(pure = true)
    default Func1<Product2<A, B>, R> tupled() {
        return x -> apply(x.$1(), x.$2());
    }

    default Func2<Supplier<A>, Supplier<B>, R> lazyInput() {
        return (a, b) -> apply(a.get(), b.get());
    }

    default <Q, W> Func2<Q, W, R> compose2(Function<? super Q, ? extends A> f1, Function<? super W, ? extends B> f2) {
        return (q, w) -> apply(f1.apply(q), f2.apply(w));
    }

    @Override
    default <U> Func2<A, B, U> andThen(Function<? super R, ? extends U> after) {
        return (a, b) -> after.apply(apply(a, b));
    }

    default Func1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
