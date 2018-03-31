/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func4;
import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

public final class Tuple4<A, B, C, D> implements Product4<A, B, C, D> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;

    public static Pattern $Tuple4(Pattern $1, Pattern $2, Pattern $3, Pattern $4) {
        return PatternFactory.gen4(Tuple4.class, $1, $2, $3, $4);
    }

    Tuple4(A $1, B $2, C $3, D $4) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
    }

    public <AA, BB, CC, DD> Tuple4<AA, BB, CC, DD> flatMap(Func4<A, B, C, D, Tuple4<AA, BB, CC, DD>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4());
    }

    @Override
    public A $1() {
        return $1;
    }

    @Override
    public B $2() {
        return $2;
    }

    @Override
    public C $3() {
        return $3;
    }

    @Override
    public D $4() {
        return $4;
    }

    @Override
    public int hashCode() {
        return Objects.hash($1(), $2(), $3(), $4());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
            .cast(Tuple4.class)
            .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                    && Objects.equals($4(), o.$4()))
            .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1() + ", " + $2() + ", " + $3() + ", " + $4() + ")";
    }
}
