/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func3<A, B, C, R> {

    static <X, Y, Z, R> Func3<X, Y, Z, R> of(@NonNull Func3<X, Y, Z, R> reference) {
        return Objects.requireNonNull(reference, "reference");
    }

    static <X, Y, Z, R> Func3<X, Y, Z, R> narrow(@NonNull Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::apply;
    }

    static <X, Y, Z, R> Func3<X, Y, Z, Value<R>> lift(@NonNull Func3<? super X, ? super Y, ? super Z, ? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc3.lift(func::apply);
    }

    default Func1<A, Func1<B, Func1<C, R>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    default <U> Func3<A, B, C, U> andThen(@NonNull Function<? super R, ? extends U> next) {
        Objects.requireNonNull(next, "next");
        return (s, t, u) -> next.apply(apply(s, t, u));
    }

    default Func3<Supplier<A>, Supplier<B>, Supplier<C>, R> lazyInput() {
        return (a, b, c) -> apply(a.get(), b.get(), c.get());
    }

    R apply(A a, B b, C c);

    default Func2<B, C, R> apply(A a) {
        return (b, c) -> apply(a, b, c);
    }

    default Func1<C, R> apply(A a, B b) {
        return c -> apply(a, b, c);
    }
}
