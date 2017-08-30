/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func5;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {

    E $5;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4, Pattern $5) {
        return patternFor(Tuple5.class)
                .addTest($1, x -> x.$1())
                .addTest($2, x -> x.$2())
                .addTest($3, x -> x.$3())
                .addTest($4, x -> x.$4())
                .addTest($5, x -> x.$5())
                .build();
    }

    protected Tuple5(A $1, B $2, C $3, D $4, E $5) {
        super($1, $2, $3, $4);
        this.$5 = $5;
    }

    {
        supplierIterable.add(this::$5);
    }

    public <AA, BB, CC, DD, EE> Tuple5<AA, BB, CC, DD, EE> flatMap(Func5<A, B, C, D, E, Tuple5<AA, BB, CC, DD, EE>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5());
    }

    public <O> O map(Func5<A, B, C, D, E, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5());
    }

    @Override
    public int size() {
        return 5;
    }

    public E $5() {
        return $5;
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
                .append(')')
                .toString();
    }
}
