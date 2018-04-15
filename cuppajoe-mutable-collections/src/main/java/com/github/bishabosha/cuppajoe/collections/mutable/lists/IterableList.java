/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.lists;

import java.util.List;
import java.util.function.Predicate;

public interface IterableList<E> extends List<E> {
    void reset();

    List<E> resetCurrentTop();

    List<E> resetCurrentTail();

    E currentValue();

    E replaceCurrent(E element);

    void insertCurrent(E element);

    int currentIndex();

    E removeCurrentIf(Predicate<E> condition);

    E removeCurrentValue();

    E advanceForward();

    E advanceBackward();

    boolean setCurrent();

    boolean setNext();

    boolean setPrevious();

    Iterable<E> reversed();

    void goToIndex(int index);
}