/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func3;
import io.cuppajoe.match.Pattern;
import io.cuppajoe.match.PatternFactory;

import java.util.Objects;

public final class Tuple3<A, B, C> implements Product3<A, B, C> {

    private final A $1;
    private final B $2;
    private final C $3;

    public static Pattern $Tuple3(Pattern $1, Pattern $2, Pattern $3) {
        return PatternFactory.gen3(Tuple3.class, $1, $2, $3);
    }

    Tuple3(A $1, B $2, C $3) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
    }

    public <AA, BB, CC> Tuple3<AA, BB, CC> flatMap(Func3<A, B, C, Tuple3<AA, BB, CC>> mapper) {
        return mapper.apply($1(), $2(), $3());
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
    public int hashCode() {
        return Objects.hash($1(), $2(), $3());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
            .cast(Tuple3.class)
            .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3()))
            .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1() + ", " + $2() + ", " + $3() + ")";
    }
}
