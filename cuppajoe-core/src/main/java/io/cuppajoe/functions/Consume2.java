/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple2;
import io.cuppajoe.tuples.Unit;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume2<A, B> extends BiConsumer<A, B> {

    default void apply(A a, B b) {
        accept(a, b);
    }

    static <X, Y> Consume2<X, Y> of(BiConsumer<X, Y> reference) {
        return reference::accept;
    }

    static <X, Y> Consume2<X, Y> narrow(BiConsumer<? super X, ? super Y> func) {
        return func::accept;
    }

    static <X, Y> Func2<X, Y, Try<Unit>> lift(BiConsumer<? super X, ? super Y> func) {
        return CheckedConsume2.lift(func::accept);
    }

    default Func1<A, Consume1<B>> curried() {
        return x -> y -> accept(x, y);
    }

    default Consume1<Tuple2<A, B>> tupled() {
        return x -> accept(x.$1, x.$2);
    }

    default Consume2<Supplier<A>, Supplier<B>> lazyInput() {
        return (a, b) -> accept(a.get(), b.get());
    }

    default Consume1<B> apply(A a) {
        return b -> accept(a, b);
    }
}
