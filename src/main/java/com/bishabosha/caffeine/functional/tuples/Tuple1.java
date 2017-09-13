/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func1;

import java.util.Objects;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public final class Tuple1<A> implements Product1<A> {

    private final A $1;

    public static Pattern Tuple1(Pattern $1) {
        return patternFor(Product1.class).atomic($1, Product1::$1);
    }

    public static <A> Tuple1<A> of(A $1) {
        return new Tuple1<>($1);
    }

    protected Tuple1(A $1) {
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
        return $1().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return Option.ofUnknown(obj)
             .cast(Product1.class)
             .map(o -> Objects.equals($1(), o.$1()))
             .orElse(false);
    }

    public String toString() {
        return "("+$1()+")";
    }
}
