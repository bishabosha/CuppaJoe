/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func2;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple2<A, B> extends Tuple1<A> {
    private B $2;

    public static Pattern Tuple(Pattern $1, Pattern $2) {
        return patternFor(Tuple2.class).testTwo(
            Tuples.Tuple($1, x -> x.$1()),
            Tuples.Tuple($2, x -> x.$2())
        );
    }

    protected Tuple2(A $1, B $2) {
        super($1);
        this.$2 = $2;
    }

    {
        supplierIterable.add(this::$2);
    }

    public <AA, BB> Tuple2<AA, BB> flatMap(Func2<A, B, Tuple2<AA, BB>> mapper) {
        return mapper.apply($1(), $2());
    }

    public <O> O map(Func2<A, B, O> mapper) {
        return mapper.apply($1(), $2());
    }

    @Override
    public int size() {
        return 2;
    }

    public B $2() {
        return $2;
    }

    public String toString() {
        return "(" + $1() + ", " + $2() + ")";
    }
}
