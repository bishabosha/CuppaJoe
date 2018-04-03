package io.cuppajoe.collections.immutable;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Bunch<E> extends Iterable<E> {

    default boolean contains(E obj) {
        return anyMatch(e -> Objects.equals(e, obj));
    }

    default boolean anyMatch(Predicate<? super E> test) {
        for (var elem: this) {
            if (test.test(elem)) {
                return true;
            }
        }
        return false;
    }

    default <R> boolean anyMatch(Iterable<R> other, BiPredicate<? super E, ? super R> test) {
        var otherVals = other.iterator();
        for (var elem: this) {
            if (!otherVals.hasNext()) {
                return false;
            }
            if (test.test(elem, otherVals.next())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(Predicate<? super E> test) {
        for (var elem: this) {
            if (!test.test(elem)) {
                return false;
            }
        }
        return true;
    }

    default <R> boolean allMatch(Iterable<R> other, BiPredicate<? super E, ? super R> test) {
        return allMatch(false, other, test);
    }

    default <R> boolean allMatchExhaustive(Iterable<R> other, BiPredicate<? super E, ? super R> test) {
        return allMatch(true, other, test);
    }

    private <R> boolean allMatch(boolean checkExhaustion, Iterable<R> other, BiPredicate<? super E, ? super R> test) {
        var otherVals = other.iterator();
        for (var elem: this) {
            if (!otherVals.hasNext() || !test.test(elem, otherVals.next())) {
                return false;
            }
        }
        return !(checkExhaustion && otherVals.hasNext());
    }
}
