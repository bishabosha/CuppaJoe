/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func4;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
    private D $4;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3, Pattern $4) {
        return patternFor(Tuple4.class)
                .addTest($1, x -> x.$1())
                .addTest($2, x -> x.$2())
                .addTest($3, x -> x.$3())
                .addTest($4, x -> x.$4())
                .build();
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> of(A $1, B $2, C $3, D $4) {
        return new Tuple4<>($1, $2, $3, $4);
    }

    protected Tuple4(A $1, B $2, C $3, D $4) {
        super($1, $2, $3);
        this.$4 = $4;
    }

    {
        supplierIterable.add(this::$4);
    }

    public <AA, BB, CC, DD> Tuple4<AA, BB, CC, DD> flatMap(Func4<A, B, C, D, Tuple4<AA, BB, CC, DD>> mapper) {
        return mapper.apply($1(), $2(), $3(), $4());
    }

    public <O> O map(Func4<A, B, C, D, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4());
    }

    @Override
    public int size() {
        return 4;
    }

    public D $4() {
        return $4;
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
                .append(')')
                .toString();
    }
}
