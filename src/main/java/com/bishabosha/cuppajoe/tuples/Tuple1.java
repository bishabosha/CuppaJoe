/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;

import java.util.Objects;

import static com.bishabosha.cuppajoe.API.Option;

public final class Tuple1<A> implements Product1<A> {

    private final A $1;

    private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Tuple1.class);

    public static Pattern $Tuple1(Pattern $1) {
        return PATTERN.apply($1);
    }

    public static <A> Tuple1<A> of(A $1) {
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
        if (obj == this) {
            return true;
        }
        return Option(obj)
             .cast(Product1.class)
             .map(o -> Objects.equals($1(), o.$1()))
             .orElse(false);
    }

    public String toString() {
        return "("+$1()+")";
    }
}
