/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Unit implements Product0 {

    public final static Unit UNIT = new Unit();

    private Unit() {
    }

    @NotNull
    @Contract(pure = true)
    public static Pattern ¥Unit() {
        return x -> x == UNIT ? Pattern.PASS : Pattern.FAIL;
    }

    @NotNull
    @Contract(pure = true)
    public static Unit getInstance() {
        return UNIT;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Unit && obj == UNIT;
    }

    @Override
    public String toString() {
        return "()";
    }
}
