/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Tuple1;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Consume1<A> extends Consumer<A> {

    default void apply(A a) {
        accept(a);
    }

    static <X> Consume1<X> of(@NonNull Consumer<X> reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::accept;
    }

    static <X> Consume1<X> narrow(@NonNull Consumer<? super X> func) {
        Objects.requireNonNull(func, "func");
        return func::accept;
    }

    static <X> Func1<X, Try<Unit>> lift(@NonNull Consumer<? super X> func) {
        Objects.requireNonNull(func, "func");
        return CheckedConsume1.lift(func::accept);
    }

    default Consume1<Supplier<A>> lazyInput() {
        return x -> accept(x.get());
    }

    default Consume1<Tuple1<A>> tupled() {
        return x -> accept(x.$1);
    }
}
