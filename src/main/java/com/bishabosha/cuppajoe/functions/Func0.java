/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.functions;

import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.tuples.Apply0;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@FunctionalInterface
public interface Func0<R> extends Supplier<R> {

    default R apply() {
        return get();
    }

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
    static <R> Func0<Try<R>> lift(Supplier<? extends R> func) {
        return CheckedFunc0.lift(func::get);
    }

    @Contract(pure = true)
    default Apply0 tupled() {
        return x -> get();
    }
}
