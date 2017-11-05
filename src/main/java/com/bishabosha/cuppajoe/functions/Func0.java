/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply0;
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
    default Apply0 applied() {
        return x -> get();
    }
}
