package com.bishabosha.cuppajoe;

import java.util.function.BiFunction;

public interface Foldable<E> extends Iterable<E> {
    default <O> O foldLeft(O accumulator, BiFunction<O, E, O> mapper) {
        return foldOver(this, accumulator, mapper);
    }

    <O> O foldRight(O accumulator, BiFunction<O, E, O> mapper);

    static <E, A> A foldOver(Iterable<E> that, A accumulator, BiFunction<A, E, A> mapper) {
        for (var elem: that) {
            accumulator = mapper.apply(accumulator, elem);
        }
        return accumulator;
    }
}
