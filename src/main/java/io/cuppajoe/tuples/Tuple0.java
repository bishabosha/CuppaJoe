/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tuple0 implements Product0 {

    private final static Tuple0 TUPLE_0 = new Tuple0();

    private Tuple0() {
    }

    @NotNull
    @Contract(pure = true)
    public static Pattern ¥Tuple0() {
        return x -> x == TUPLE_0 ? Pattern.PASS : Pattern.FAIL;
    }

    @NotNull
    @Contract(pure = true)
    public static Product0 getInstance() {
        return TUPLE_0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Tuple0 && obj == TUPLE_0;
    }

    @Override
    public String toString() {
        return "()";
    }
}
