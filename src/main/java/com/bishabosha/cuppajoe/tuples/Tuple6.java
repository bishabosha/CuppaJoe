/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.functions.Func6;
import com.bishabosha.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;

public final class Tuple6<A, B, C, D, E, F> implements Product6<A, B, C, D, E, F> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;
    private final F $6;

    private static final Func6<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern>
        PATTERN = PatternFactory.gen6(Tuple6.class);

    public static Pattern $Tuple6(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6) {
            return PATTERN.apply($1, $2, $3, $4, $5, $6);
        }

    public static <A, B, C, D, E, F>
    Tuple6<A, B, C, D, E, F>
    of(A $1, B $2, C $3, D $4, E $5, F $6) {
        return new Tuple6<>($1, $2, $3, $4, $5, $6);
    }

    private Tuple6(A $1, B $2, C $3, D $4, E $5, F $6) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
    }

    public <AA, BB, CC, DD, EE, FF> Tuple6<AA, BB, CC, DD, EE, FF> flatMap(Func6<A, B, C, D, E, F, Tuple6<AA, BB, CC, DD, EE, FF>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6());
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
    public E $5() {
        return $5;
    }

    @Override
    public F $6() {
        return $6;
    }

    @Override
    public int hashCode() {
        return Objects.hash($1(), $2(), $3(), $4(), $5(), $6());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return Option(obj)
                .cast(Tuple6.class)
                .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                        && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()) && Objects.equals($6(), o.$6()))
                .orElse(false);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append('(')
                .append($1())
                .append(", ")
                .append($2())
                .append(", ")
                .append($3())
                .append(", ")
                .append($4())
                .append(", ")
                .append($5())
                .append(", ")
                .append($6())
                .append(')')
                .toString();
    }
}
