/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

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

    static <X> Func1<X, Value<Void>> lift(@NonNull Consumer<? super X> func) {
        Objects.requireNonNull(func, "func");
        return CheckedConsume1.lift(func::accept);
    }

    default Consume1<Supplier<A>> lazyInput() {
        return x -> accept(x.get());
    }
}
