package io.cuppajoe.collections.immutable;

import io.cuppajoe.annotation.NonNull;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface Bunch<E> extends Iterable<E> {

    default boolean contains(E obj) {
        return anyMatch(e -> Objects.equals(e, obj));
    }

    default boolean anyMatch(@NonNull Predicate<? super E> test) {
        Objects.requireNonNull(test, "test");
        for (var elem : this) {
            if (test.test(elem)) {
                return true;
            }
        }
        return false;
    }

    default <R> boolean anyMatch(@NonNull Iterable<R> other, @NonNull BiPredicate<? super E, ? super R> test) {
        Objects.requireNonNull(other, "other");
        Objects.requireNonNull(test, "test");
        var otherVals = other.iterator();
        for (var elem : this) {
            if (!otherVals.hasNext()) {
                return false;
            }
            if (test.test(elem, otherVals.next())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(@NonNull Predicate<? super E> test) {
        Objects.requireNonNull(test, "test");
        for (var elem : this) {
            if (!test.test(elem)) {
                return false;
            }
        }
        return true;
    }

    default <R> boolean allMatch(@NonNull Iterable<R> other, @NonNull BiPredicate<? super E, ? super R> test) {
        return allMatch(false, other, test);
    }

    default <R> boolean allMatchExhaustive(@NonNull Iterable<R> other, @NonNull BiPredicate<? super E, ? super R> test) {
        return allMatch(true, other, test);
    }

    private <R> boolean allMatch(boolean checkExhaustion, @NonNull Iterable<R> other, @NonNull BiPredicate<? super E, ? super R> test) {
        Objects.requireNonNull(other, "other");
        Objects.requireNonNull(test, "test");
        var otherVals = other.iterator();
        for (var elem : this) {
            if (!otherVals.hasNext() || !test.test(elem, otherVals.next())) {
                return false;
            }
        }
        return !(checkExhaustion && otherVals.hasNext());
    }
}
