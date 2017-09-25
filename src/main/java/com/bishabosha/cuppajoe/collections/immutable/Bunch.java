package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Value;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Bunch<O> extends Value<O> {

    @Override
    default boolean isAtMaxSingleElement() {
        return false;
    }

    default boolean contains(O obj) {
        return anyMatch(e -> Objects.equals(e, obj));
    }

    default boolean anyMatch(Predicate<? super O> test) {
        for (O elem: this) {
            if (test.test(elem)) {
                return true;
            }
        }
        return false;
    }

    default <R> boolean anyMatch(Iterable<R> other, BiPredicate<? super O, ? super R> test) {
        Iterator<R> otherVals = other.iterator();
        for (O elem: this) {
            if (!otherVals.hasNext()) {
                return false;
            }
            if (test.test(elem, otherVals.next())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(Predicate<? super O> test) {
        for (O elem: this) {
            if (!test.test(elem)) {
                return false;
            }
        }
        return true;
    }

    default <R> boolean allMatch(Iterable<R> other, BiPredicate<? super O, ? super R> test) {
        Iterator<R> otherVals = other.iterator();
        for (O elem: this) {
            if (!otherVals.hasNext() || !test.test(elem, otherVals.next())) {
                return false;
            }
        }
        return true;
    }
}
