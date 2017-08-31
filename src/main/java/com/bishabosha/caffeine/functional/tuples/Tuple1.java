/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func1;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple1<A> extends Tuple {

    private A $1;

    public static Pattern Tuple(Pattern $1) {
        return patternFor(Tuple1.class).atomic($1, x -> x.$1());
    }

    protected Tuple1(A $1) {
        this.$1 = $1;
    }

    {
        supplierIterable.add(this::$1);
    }

    @Override
    public int size() {
        return 1;
    }

    public A $1() {
        return $1;
    }

    public <AA> Tuple1<AA> flatMap(Func1<A, Tuple1<AA>> mapper) {
        return mapper.apply($1());
    }

    public <O> O map(Func1<A, O> mapper) {
        return mapper.apply($1());
    }

    public String toString() {
        return "("+$1()+")";
    }
}
