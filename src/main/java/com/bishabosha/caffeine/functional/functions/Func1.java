/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Apply1;
import com.bishabosha.caffeine.functional.tuples.Product1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Func1<A, R> extends Function<A, R> {

    @NotNull
    @Contract(pure = true)
    static <X, R> Func1<X, R> of(Function<X, R> reference) {
        return reference::apply;
    }

    @Contract(pure = true)
    static <X,R> Func1<X,R> narrow(Function<? super X, ? extends R> func) {
        return func::apply;
    }

    @Contract(pure = true)
    static <X, R> Func1<X, Option<R>> lift(Function<? super X, ? extends R> func) {
        return x -> Try.<R>narrow(Try.of(() -> func.apply(x))).get();
    }

    @Contract(pure = true)
    default Func1<Product1<A>, R> tupled() {
        return x -> apply(x.$1());
    }

    default Apply1<A, R> applied() {
        return x -> tupled().apply(x.unapply());
    }
}
