/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple0;
import org.jetbrains.annotations.Contract;

public interface CheckedFunc0<R> {
    R apply() throws Throwable;

    @Contract(pure = true)
    static <R> CheckedFunc0<R> of(CheckedFunc0<R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func0<Option<R>> lifted() {
        return () -> {
            try {
                return Option.ofUnknown(apply());
            } catch (Throwable e) {
                return Option.nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<Tuple0, R> tupled() {
        return t -> apply();
    }
}
