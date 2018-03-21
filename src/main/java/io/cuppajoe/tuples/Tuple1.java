/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

public final class Tuple1<A> implements Product1<A> {

    private final A $1;

    private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Tuple1.class);

    public static Pattern $Tuple1(Pattern $1) {
        return PATTERN.apply($1);
    }

    public static <A> Product1<A> of(A $1) {
        return new Tuple1<>($1);
    }

    private Tuple1(A $1) {
        this.$1 = $1;
    }

    public <AA> Tuple1<AA> flatMap(Func1<A, Tuple1<AA>> mapper) {
        return mapper.apply($1());
    }

    public A $1() {
        return $1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode($1());
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
             .cast(Product1.class)
             .map(o -> Objects.equals($1(), o.$1()))
             .orElse(false);
    }

    public String toString() {
        return "(" + $1() + ")";
    }
}
