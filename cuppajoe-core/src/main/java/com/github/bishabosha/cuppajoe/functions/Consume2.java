/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume2<A, B> extends BiConsumer<A, B> {

    default void apply(A a, B b) {
        accept(a, b);
    }

    static <X, Y> Consume2<X, Y> of(@NonNull BiConsumer<X, Y> reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::accept;
    }

    static <X, Y> Consume2<X, Y> narrow(@NonNull BiConsumer<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
        return func::accept;
    }

    static <X, Y> Func2<X, Y, Try<Unit>> lift(@NonNull BiConsumer<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
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
