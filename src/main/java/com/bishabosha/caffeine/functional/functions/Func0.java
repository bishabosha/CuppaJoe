/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.control.Try;
import com.bishabosha.caffeine.functional.tuples.Apply0;
import com.bishabosha.caffeine.functional.tuples.Product0;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface Func0<R> extends Supplier<R> {

    @NotNull
    @Contract(pure = true)
    static <R> Func0<R> of(Supplier<R> reference) {
        return reference::get;
    }

    @Contract(pure = true)
    static <R> Func0<R> narrow(Supplier<? extends R> func) {
        return func::get;
    }

    @Contract(pure = true)
    static <R> Func0<Option<R>> lift(Supplier<? extends R> func) {
        return Try.<R>narrow(Try.of(func::get))::get;
    }

    @Contract(pure = true)
    default Func1<Product0, R> tupled() {
        return x -> get();
    }

    default Apply0 applied() {
        return x -> tupled().apply(x.unapply());
    }
}
