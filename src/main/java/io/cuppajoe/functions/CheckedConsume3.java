/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.Unit;
import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Product3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedConsume3<A, B, C> {

    void apply(A a, B b, C c) throws Exception;

    @NotNull
    @Contract(pure = true)
    static <X, Y, Z> CheckedConsume3<X, Y, Z> of(CheckedConsume3<X, Y, Z> reference) {
        return reference;
    }

    @NotNull
    @Contract(pure = true)
    static <X, Y, Z> CheckedConsume3<X, Y, Z> narrow(CheckedConsume3<? super X, ? super Y, ? super Z> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <X, Y, Z> Func3<X, Y, Z, Try<Unit>> lift(CheckedConsume3<? super X, ? super Y, ? super Z> func) {
        return (x, y, z) -> Try.of(() -> {
            func.apply(x, y, z);
            return Unit.INSTANCE;
        });
    }

    @Contract(pure = true)
    default Func1<A, Func1<B, CheckedConsume1<C>>> curried() {
        return x -> y -> z -> apply(x, y, z);
    }

    @Contract(pure = true)
    default CheckedConsume1<Product3<A, B, C>> tupled() {
        return x -> apply(x.$1(), x.$2(), x.$3());
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
