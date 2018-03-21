/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Product1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume1<A> extends Consumer<A> {

    default void apply(A a) {
        accept(a);
    }

    @NotNull
    @Contract(pure = true)
    static <X> Consume1<X> of(Consumer<X> reference) {
        return reference::accept;
    }

    @Contract(pure = true)
    static <X> Consume1<X> narrow(Consumer<? super X> func) {
        return func::accept;
    }

    @Contract(pure = true)
    static <X> Func1<X, Try<Void>> lift(Consumer<? super X> func) {
        return CheckedConsume1.lift(func::accept);
    }

    default Consume1<Supplier<A>> lazyInput() {
        return x -> accept(x.get());
    }

    default Consume1<Product1<A>> tupled() {
        return x -> accept(x.$1());
    }
}
