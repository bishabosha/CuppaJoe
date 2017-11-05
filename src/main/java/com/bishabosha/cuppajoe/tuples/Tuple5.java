/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.functions.Func5;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;

public final class Tuple5<A, B, C, D, E> implements Product5<A, B, C, D, E> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;

    private static final Func5<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern>
        PATTERN = PatternFactory.gen5(Tuple5.class);

    public static Pattern $Tuple5(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5) {
        return PATTERN.apply($1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E>
    Tuple5<A, B, C, D, E>
    of(A $1, B $2, C $3, D $4, E $5) {
        return new Tuple5<>($1, $2, $3, $4, $5);
    }

    private Tuple5(A $1, B $2, C $3, D $4, E $5) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
    }

    public <AA, BB, CC, DD, EE> Tuple5<AA, BB, CC, DD, EE> flatMap(Func5<A, B, C, D, E, Tuple5<AA, BB, CC, DD, EE>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5());
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
    public int hashCode() {
        return Objects.hash($1(), $2(), $3(), $4(), $5());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return Option(obj)
            .cast(Tuple5.class)
            .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                    && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()))
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
                .append(')')
                .toString();
    }
}
