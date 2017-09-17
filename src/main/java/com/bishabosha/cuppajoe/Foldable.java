package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.functions.Func2;

public interface Foldable<E> extends Iterable<E> {

    default <A> A foldLeft(A accumulator, Func2<E, A, A> mapper) {
        return foldOver(this, accumulator, mapper);
    }

    <A> A foldRight(A accumulator, Func2<E, A, A> mapper);

    default <A> A fold(A accumulator, Func2<E, A, A> mapper) {
        return foldOver(this, accumulator, mapper);
    }

    static <E, A> A foldOver(Iterable<E> that, A accumulator, Func2<E, A, A> mapper) {
        for (E elem: that) {
            accumulator = mapper.apply(elem, accumulator);
        }
        return accumulator;
    }
}
