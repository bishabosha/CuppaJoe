/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func8;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

public class Tuple8<A, B, C, D, E, F, G, H> implements Product8<A, B, C, D, E, F, G, H> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;
    private final F $6;
    private final G $7;
    private final H $8;

    private static final Func8<Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern, Pattern>
        PATTERN = PatternFactory.gen8(Tuple8.class);

    public static Pattern $Tuple8(
        Pattern $1, Pattern $2, Pattern $3, Pattern $4,
        Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
            return PATTERN.apply($1, $2, $3, $4, $5, $6, $7, $8);
        }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return new Tuple8<>($1, $2, $3, $4, $5, $6, $7, $8);
    }

    private Tuple8(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
        this.$7 = $7;
        this.$8 = $8;
    }

    public <AA, BB, CC, DD, EE, FF, GG, HH> Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>
    flatMap(Func8<A, B, C, D, E, F, G, H, Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7(), $8());
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
    public G $7() {
        return $7;
    }

    @Override
    public H $8() {
        return $8;
    }

    @Override
    public int hashCode() {
        return Objects.hash($1(), $2(), $3(), $4(), $5(), $6(), $7(), $8());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple8.class)
                .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                        && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()) && Objects.equals($6(), o.$6())
                        && Objects.equals($7(), o.$7()) && Objects.equals($8(), o.$8()))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1() + ", " + $2() + ", " + $3() + ", " + $4() + ", " + $5() + ", " + $6() + ", " + $7() + ", " + $8() + ")";
    }
}
