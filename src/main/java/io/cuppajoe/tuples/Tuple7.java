/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func7;
import io.cuppajoe.match.Pattern;
import io.cuppajoe.match.PatternFactory;

import java.util.Objects;

public final class Tuple7<A, B, C, D, E, F, G> implements Product7<A, B, C, D, E, F, G> {

    private final A $1;
    private final B $2;
    private final C $3;
    private final D $4;
    private final E $5;
    private final F $6;
    private final G $7;

    public static Pattern $Tuple7(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5, Pattern $6, Pattern $7) {
        return PatternFactory.gen7(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    Tuple7(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
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
        return obj == this || Option.of(obj)
                .cast(Tuple7.class)
                .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()) && Objects.equals($3(), o.$3())
                        && Objects.equals($4(), o.$4()) && Objects.equals($5(), o.$5()) && Objects.equals($6(), o.$6())
                        && Objects.equals($7(), o.$7()))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1() + ", " + $2() + ", " + $3() + ", " + $4() + ", " + $5() + ", " + $6() + ", " + $7() + ")";
    }
}
