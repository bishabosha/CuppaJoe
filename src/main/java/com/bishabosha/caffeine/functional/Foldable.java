package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.functions.Func2;

public interface Foldable<E> extends Iterable<E> {

    default <A> A foldRight(A accumulator, Func2<E, A, A> mapper) {
        for (E elem: reverse()) {
            accumulator = mapper.apply(elem, accumulator);
        }
        return accumulator;
    }

    default <A> A foldLeft(A accumulator, Func2<E, A, A> mapper) {
        for (E elem: this) {
            accumulator = mapper.apply(elem, accumulator);
        }
        return accumulator;
    }

    <I extends Iterable<E>> I reverse();
}
