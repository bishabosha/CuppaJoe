package io.cuppajoe.collections.mutable.sequences;

import java.util.List;
import java.util.function.Predicate;

public final class PredicateFor {

    private PredicateFor() {
    }

    public static <T> Predicate<T> alwaysTrue() {
        return x -> true;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return x -> false;
    }

    public static Predicate<Integer> isPalindromeInteger() {
        return x -> {
            var str = new StringBuilder(x.toString());
            return str.equals(str.reverse());
        };
    }

    public static Predicate<Long> isMultipleOf(List<Long> values) {
        return isMultipleOf(values, alwaysFalse());
    }

    public static Predicate<Long> isMultipleOf(List<Long> values, Predicate<Long> stoppingCondition) {
        return t -> {
            for (var a : values) {
                if (stoppingCondition.test(a)) {
                    return false;
                }
                if (t % a == 0) return true;
            }
            return false;
        };
    }
}
