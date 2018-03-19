/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
    static <X, Y, R> Func2<X, Y, Option<R>> lift(BiFunction<? super X, ? super Y, ? extends R> func) {
        return (x, y) -> Try.<R>of(() -> func.apply(x, y)).lift();
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, R>> curried() {
        return x -> y -> apply(x, y);
    }

    @Contract(pure = true)
    default Apply2<A, B, R> applied() {
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
