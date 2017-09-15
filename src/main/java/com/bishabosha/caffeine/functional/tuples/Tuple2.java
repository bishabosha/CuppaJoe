/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.API;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.functions.Func2;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

import static com.bishabosha.caffeine.functional.API.Option;
import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;

public final class Tuple2<A, B> implements Product2<A, B> {

    private final A $1;
    private final B $2;

    public static Pattern Tuple2(Pattern $1, Pattern $2) {
        return patternFor(Tuple2.class).testTwo(
            of($1, Product2::$1),
            of($2, Product2::$2)
        );
    }

    public static <A, B> Tuple2<A, B> of(A $1, B $2) {
        return new Tuple2<>($1, $2);
    }

    protected Tuple2(A $1, B $2) {
        this.$1 = $1;
        this.$2 = $2;
    }

    public <AA, BB> Tuple2<AA, BB> flatMap(Func2<A, B, Tuple2<AA, BB>> mapper) {
        return mapper.apply($1(), $2());
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
        if (obj == this) {
            return true;
        }
        return Option(obj)
            .cast(Tuple2.class)
            .map(o -> Objects.equals($1(), o.$1()) && Objects.equals($2(), o.$2()))
            .orElse(false);
    }

    public String toString() {
        return "(" + $1() + ", " + $2() + ")";
    }
}
