/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.match.Pattern;
import io.cuppajoe.match.PatternFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Tuple0 implements Product0 {

    INSTANCE;

    @NotNull
    @Contract(pure = true)
    public static Pattern _Tuple0() {
        return PatternFactory.gen0(INSTANCE);
    }

    @Override
    public String toString() {
        return "()";
    }
}
