/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func6;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple6<A, B, C, D, E, F> extends Tuple5<A, B, C, D, E> {

    F $6;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4,
                                Pattern $5, Pattern $6) {
        return patternFor(Tuple6.class)
                .addTest($1, x -> x.$1())
                .addTest($2, x -> x.$2())
                .addTest($3, x -> x.$3())
                .addTest($4, x -> x.$4())
                .addTest($5, x -> x.$5())
                .addTest($6, x -> x.$6())
                .build();
    }

    protected Tuple6(A $1, B $2, C $3, D $4, E $5, F $6) {
        super($1, $2, $3, $4, $5);
        this.$6 = $6;
    }

    {
        supplierIterable.add(this::$6);
    }

    public <AA, BB, CC, DD, EE, FF> Tuple6<AA, BB, CC, DD, EE, FF> flatMap(Func6<A, B, C, D, E, F, Tuple6<AA, BB, CC, DD, EE, FF>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6());
    }

    public <O> O map(Func6<A, B, C, D, E, F, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6());
    }

    @Override
    public int size() {
        return 6;
    }

    public F $6() {
        return $6;
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
                .append(')')
                .toString();
    }
}
