/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.functions;

import io.cuppajoe.control.Try;
import io.cuppajoe.tuples.Unit;

import java.util.function.Supplier;

@FunctionalInterface
public interface Func0<R> extends Supplier<R> {

    default R apply() {
        return get();
    }

    static <R> Func0<R> of(Supplier<R> reference) {
        return reference::get;
    }

    static <R> Func0<R> narrow(Supplier<? extends R> func) {
        return func::get;
    }

    static <R> Func0<Try<R>> lift(Supplier<? extends R> func) {
        return CheckedFunc0.lift(func::get);
    }

    default Func1<Unit, R> tupled() {
        return x -> get();
    }
}
