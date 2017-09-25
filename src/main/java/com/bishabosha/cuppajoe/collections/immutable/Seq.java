package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.tuples.Product2;

public interface Seq<E> extends Bunch<E>, Foldable<E> {

    List<E> push(E elem);

    Option<? extends Product2<E, ? extends Seq<E>>> pop();

    E get(int n);

    E head();

    Seq<E> tail();

    Seq<E> take(int limit);

    Seq<E> takeRight(int limit);

    Seq<E> subsequence(int from, int limit);

    Seq<E> reverse();

    Seq<E> append(E elem);

    Seq<E> remove(E elem);

    @Override
    default <A> A foldRight(A accumulator, Func2<E, A, A> mapper) {
        return reverse().fold(accumulator, mapper);
    }
}
