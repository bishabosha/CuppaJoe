/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume3<A, B, C> {

    void apply(A a, B b, C c) throws Exception;

    static <X, Y, Z> CheckedConsume3<X, Y, Z> of(@NonNull CheckedConsume3<X, Y, Z> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y, Z> CheckedConsume3<X, Y, Z> narrow(@NonNull CheckedConsume3<? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y, Z> Func3<X, Y, Z, Value<Void>> lift(@NonNull CheckedConsume3<? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return (x, y, z) -> LiftOps.liftConsumer(() -> func.apply(x, y, z));
    }

    default Func1<A, Func1<B, CheckedConsume1<C>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default CheckedConsume3<Supplier<A>, Supplier<B>, Supplier<C>> lazyInput() {
        return (a, b, c) -> apply(a.get(), b.get(), c.get());
    }

    default CheckedConsume1<C> apply(A a, B b) {
        return c -> apply(a, b, c);
    }

    default CheckedConsume2<B, C> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }
}
