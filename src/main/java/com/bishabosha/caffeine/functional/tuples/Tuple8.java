/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func8;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple8<A, B, C, D, E, F, G, H> extends Tuple7<A, B, C, D, E, F, G> {

    H $8;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4,
                                Pattern $5, Pattern $6, Pattern $7, Pattern $8) {
        return patternFor(Tuple8.class)
                .addTest($1, x -> x.$1())
                .addTest($2, x -> x.$2())
                .addTest($3, x -> x.$3())
                .addTest($4, x -> x.$4())
                .addTest($5, x -> x.$5())
                .addTest($6, x -> x.$6())
                .addTest($7, x -> x.$7())
                .addTest($8, x -> x.$8())
                .build();
    }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return new Tuple8<>($1, $2, $3, $4, $5, $6, $7, $8);
    }

    protected Tuple8(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        super($1, $2, $3, $4, $5, $6, $7);
        this.$8 = $8;
    }

    {
        supplierIterable.add(this::$8);
    }

    public <AA, BB, CC, DD, EE, FF, GG, HH> Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>
    flatMap(Func8<A, B, C, D, E, F, G, H, Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7(), $8());
    }

    public <O> O map(Func8<A, B, C, D, E, F, G, H, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7(), $8());
    }

    @Override
    public int size() {
        return 8;
    }

    public H $8() {
        return $8;
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
                .append(", ")
                .append($8())
                .append(')')
                .toString();
    }
}
