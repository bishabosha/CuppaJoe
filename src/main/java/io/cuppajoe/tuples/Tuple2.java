/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public final class Tuple2<A, B> implements Product2<A, B> {

    private final A $1;
    private final B $2;

    public static Pattern $Tuple2(Pattern $1, Pattern $2) {
        return PatternFactory.gen2(Product2.class, $1, $2);
    }

    Tuple2(A $1, B $2) {
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
