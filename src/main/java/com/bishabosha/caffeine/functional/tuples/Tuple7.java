/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func7;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple7<A, B, C, D, E, F, G> extends Tuple6<A, B, C, D, E, F> {

    G $7;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4,
                                Pattern $5, Pattern $6, Pattern $7) {
        return patternFor(Tuple7.class)
                .addTest($1, x -> x.$1())
                .addTest($2, x -> x.$2())
                .addTest($3, x -> x.$3())
                .addTest($4, x -> x.$4())
                .addTest($5, x -> x.$5())
                .addTest($6, x -> x.$6())
                .addTest($7, x -> x.$7())
                .build();
    }

    protected Tuple7(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        super($1, $2, $3, $4, $5, $6);
        this.$7 = $7;
    }

    {
        supplierIterable.add(this::$7);
    }

    public <AA, BB, CC, DD, EE, FF, GG> Tuple7<AA, BB, CC, DD, EE, FF, GG>
    flatMap(Func7<A, B, C, D, E, F, G, Tuple7<AA, BB, CC, DD, EE, FF, GG>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7());
    }

    public <O> O map(Func7<A, B, C, D, E, F, G, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7());
    }

    @Override
    public int size() {
        return 7;
    }

    public G $7() {
        return $7;
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
