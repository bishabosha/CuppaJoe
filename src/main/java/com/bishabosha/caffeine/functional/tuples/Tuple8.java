/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.functions.Func8;

import java.util.Objects;

import static com.bishabosha.caffeine.functional.API.Option;
import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;

public class Tuple8<A, B, C, D, E, F, G, H> implements Product8<A, B, C, D, E, F, G, H> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;
    private final F $6;
    private final G $7;
    private final H $8;

    public static Pattern Tuple8(Pattern $1, Pattern $2, Pattern $3, Pattern $4,
                                 Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
        return patternFor(Tuple8.class)
                .addTest($1, Tuple8::$1)
                .addTest($2, Tuple8::$2)
                .addTest($3, Tuple8::$3)
                .addTest($4, Tuple8::$4)
                .addTest($5, Tuple8::$5)
                .addTest($6, Tuple8::$6)
                .addTest($7, Tuple8::$7)
                .addTest($8, Tuple8::$8)
                .build();
    }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return new Tuple8<>($1, $2, $3, $4, $5, $6, $7, $8);
    }

    protected Tuple8(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
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
        if (obj == this) {
            return true;
        }
        return Option(obj)
                .cast(Tuple8.class)
                .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                        && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()) && Objects.equals($6(), o.$6())
                        && Objects.equals($7(), o.$7()) && Objects.equals($8(), o.$8()))
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
                .append(", ")
                .append($7())
                .append(", ")
                .append($8())
                .append(')')
                .toString();
    }
}
