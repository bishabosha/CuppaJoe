/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.functions;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.tuples.Tuple0;
import org.jetbrains.annotations.Contract;

public interface Func0<R> {
    R apply();

    @Contract(pure = true)
    static <R> Func0<R> of(Func0<R> func) {
        return func;
    }

    @Contract(pure = true)
    default Func0<Option<R>> lifted() {
        return CheckedFunc0.of(this::apply).lifted();
    }

    @Contract(pure = true)
    default Func1<Tuple0, R> tupled() {
        return x -> apply();
    }
}
