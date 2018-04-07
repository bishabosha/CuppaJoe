/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple3;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume3<A, B, C> {

    void apply(A a, B b, C c);

    static <X, Y, Z> Consume3<X, Y, Z> of(@NonNull Consume3<X, Y, Z> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y, Z> Consume3<X, Y, Z> narrow(@NonNull Consume3<? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y, Z> Func3<X, Y, Z, Try<Unit>> lift(@NonNull Consume3<? super X, ? super Y, ? super Z> func) {
        Objects.requireNonNull(func, "func");
        return CheckedConsume3.lift(func::apply);
    }

    default Func1<A, Func1<B, Consume1<C>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default Consume1<Tuple3<A, B, C>> tupled() {
        return x -> apply(x.$1, x.$2, x.$3);
    }

    default Consume3<Supplier<A>, Supplier<B>, Supplier<C>> lazyInput() {
        return (a, b, c) -> apply(a.get(), b.get(), c.get());
    }

    default Consume1<C> apply(A a, B b) {
        return c -> apply(a, b, c);
    }

    default Consume2<B, C> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }
}
