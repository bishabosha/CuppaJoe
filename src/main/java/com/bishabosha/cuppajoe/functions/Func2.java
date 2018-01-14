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
        return (x, y) -> Try.<R>narrow(Try.of(() -> func.apply(x, y))).get();
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

    default Func1<B, R> apply(A a) {
        return b -> apply(a, b);
    }
}
