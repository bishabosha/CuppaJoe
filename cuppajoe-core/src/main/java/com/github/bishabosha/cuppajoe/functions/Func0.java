/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.functions;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.tuples.Unit;

import java.util.Objects;
import java.util.function.Supplier;

@FunctionalInterface
public interface Func0<R> extends Supplier<R> {

    default R apply() {
        return get();
    }

    static <R> Func0<R> of(@NonNull Supplier<R> reference) {
        Objects.requireNonNull(reference, "reference");
        return reference::get;
    }

    static <R> Func0<R> narrow(@NonNull Supplier<? extends R> func) {
        Objects.requireNonNull(func, "func");
        return func::get;
    }

    static <R> Func0<Try<R>> lift(@NonNull Supplier<? extends R> func) {
        Objects.requireNonNull(func, "func");
        return CheckedFunc0.lift(func::get);
    }

    default Func1<Unit, R> tupled() {
        return x -> get();
    }
}
