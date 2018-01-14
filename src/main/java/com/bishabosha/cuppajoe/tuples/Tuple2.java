/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public final class Tuple2<A, B> implements Product2<A, B> {

    private final A $1;
    private final B $2;

    private static final Func2<Pattern, Pattern, Pattern> PATTERN = PatternFactory.gen2(Tuple2.class);

    public static Pattern $Tuple2(Pattern $1, Pattern $2) {
        return PATTERN.apply($1, $2);
    }

    public static <A, B> Product2<A, B> of(A $1, B $2) {
        return new Tuple2<>($1, $2);
    }

    private Tuple2(A $1, B $2) {
        this.$1 = $1;
        this.$2 = $2;
    }

    @Contract(pure = true)
    @Override
    public A $1() {
        return $1;
    }

    @Contract(pure = true)
    @Override
    public B $2() {
        return $2;
    }

    @Override
    public int hashCode() {
        return Objects.hash($1(), $2());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
            .cast(Tuple2.class)
            .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()))
            .orElse(false);
    }

    public String toString() {
        return "(" + $1() + ", " + $2() + ")";
    }
}
