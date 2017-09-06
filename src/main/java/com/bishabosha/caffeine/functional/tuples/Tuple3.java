/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.functions.Func3;

import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;

public class Tuple3<A, B, C> extends Tuple2<A, B> {
    private C $3;

    public static Pattern Tuple(Pattern $1, Pattern $2, Pattern $3) {
        return patternFor(Tuple3.class).testThree(
            Tuple2.of($1, x -> x.$1()),
            Tuple2.of($2, x -> x.$2()),
            Tuple2.of($3, x -> x.$3())
        );
    }

    public static <A, B, C> Tuple3<A, B, C> of(A $1, B $2, C $3) {
        return new Tuple3<>($1, $2, $3);
    }

    protected Tuple3(A $1, B $2, C $3) {
        super($1, $2);
        this.$3 = $3;
    }

    {
        supplierIterable.add(this::$3);
    }

    public <AA, BB, CC> Tuple3<AA, BB, CC> flatMap(Func3<A, B, C, Tuple3<AA, BB, CC>> mapper) {
        return mapper.apply($1(), $2(), $3());
    }

    public <O> O map(Func3<A, B, C, O> mapper) {
        return mapper.apply($1(), $2(), $3());
    }

    @Override
    public int size() {
        return 3;
    }

    public C $3() {
        return $3;
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
                .append(')')
                .toString();
    }
}
