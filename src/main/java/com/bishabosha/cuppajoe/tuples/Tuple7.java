/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.functions.Func7;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;

public final class Tuple7<A, B, C, D, E, F, G> implements Product7<A, B, C, D, E, F, G> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;
    private final F $6;
    private final G $7;

    public static Pattern Tuple7(Pattern $1, Pattern $2, Pattern $3, Pattern $4,
                                 Pattern $5, Pattern $6, Pattern $7) {
        return patternFor(Tuple7.class)
                .addTest($1, Product7::$1)
                .addTest($2, Product7::$2)
                .addTest($3, Product7::$3)
                .addTest($4, Product7::$4)
                .addTest($5, Product7::$5)
                .addTest($6, Product7::$6)
                .addTest($7, Product7::$7)
                .build();
    }

    public static <A, B, C, D, E, F, G>
    Tuple7<A, B, C, D, E, F, G>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return new Tuple7<>($1, $2, $3, $4, $5, $6, $7);
    }

    protected Tuple7(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
        this.$7 = $7;
    }

    public <AA, BB, CC, DD, EE, FF, GG> Tuple7<AA, BB, CC, DD, EE, FF, GG>
    flatMap(Func7<A, B, C, D, E, F, G, Tuple7<AA, BB, CC, DD, EE, FF, GG>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7());
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
    public int hashCode() {
        return Objects.hash($1(), $2(), $3(), $4(), $5(), $6(), $7());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return Option(obj)
                .cast(Tuple7.class)
                .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                        && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()) && Objects.equals($6(), o.$6())
                        && Objects.equals($7(), o.$7()))
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
                .append(')')
                .toString();
    }
}
