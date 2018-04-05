/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Tuple1;
import io.cuppajoe.tuples.Unit;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume1<A> extends Consumer<A> {

    default void apply(A a) {
        accept(a);
    }

    static <X> Consume1<X> of(Consumer<X> reference) {
        return reference::accept;
    }

    static <X> Consume1<X> narrow(Consumer<? super X> func) {
        return func::accept;
    }

    static <X> Func1<X, Try<Unit>> lift(Consumer<? super X> func) {
        return CheckedConsume1.lift(func::accept);
    }

    default Consume1<Supplier<A>> lazyInput() {
        return x -> accept(x.get());
    }

    default Consume1<Tuple1<A>> tupled() {
        return x -> accept(x.$1);
    }
}
