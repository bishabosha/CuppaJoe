/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func3;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

public final class Tuple3<A, B, C> implements Product3<A, B, C> {

    private final A $1;
    private final B $2;
    private final C $3;

    private static final Func3<Pattern, Pattern, Pattern, Pattern> PATTERN = PatternFactory.gen3(Tuple3.class);

    public static Pattern $Tuple3(Pattern $1, Pattern $2, Pattern $3) {
        return PATTERN.apply($1, $2, $3);
    }

    public static <A, B, C> Tuple3<A, B, C> of(A $1, B $2, C $3) {
        return new Tuple3<>($1, $2, $3);
    }

    private Tuple3(A $1, B $2, C $3) {
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
