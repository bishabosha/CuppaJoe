/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume2<A, B> {

    void apply(A a, B b) throws Exception;

    static <X, Y> CheckedConsume2<X, Y> of(@NonNull CheckedConsume2<X, Y> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y> CheckedConsume2<X, Y> narrow(@NonNull CheckedConsume2<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y> Func2<X, Y, Value<Void>> lift(@NonNull CheckedConsume2<? super X, ? super Y> func) {
        Objects.requireNonNull(func, "func");
        return (x, y) -> LiftOps.liftConsumer(() -> func.apply(x, y));
    }

    default Func1<A, CheckedConsume1<B>> curried() {
        return x -> y -> apply(x, y);
    }

    default CheckedConsume2<Supplier<A>, Supplier<B>> lazyInput() {
        return (a, b) -> apply(a.get(), b.get());
    }

    default CheckedConsume1<B> apply(A a) {
        return b -> apply(a, b);
    }
}
