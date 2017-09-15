/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Unit;
import org.jetbrains.annotations.Contract;

import static com.bishabosha.caffeine.functional.API.*;

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
                return Option(apply());
            } catch (Throwable e) {
                return Nothing();
            }
        };
    }

    @Contract(pure = true)
    default CheckedFunc1<Unit, R> tupled() {
        return t -> apply();
    }
}
