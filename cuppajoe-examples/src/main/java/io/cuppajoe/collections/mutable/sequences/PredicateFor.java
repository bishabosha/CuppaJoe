package io.cuppajoe.collections.mutable.sequences;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public final class PredicateFor {

    private PredicateFor() {
    }

    @NotNull
    @Contract(pure = true)
    public static <T> Predicate<T> alwaysTrue() {
        return x -> true;
    }

    @NotNull
    @Contract(pure = true)
    public static <T> Predicate<T> alwaysFalse() {
        return x -> false;
    }

    @NotNull
    @Contract(pure = true)
    public static Predicate<Integer> isPalindromeInteger() {
        return x -> {
            var str = new StringBuilder(x.toString());
            return str.equals(str.reverse());
        };
    }

    @NotNull
    @Contract(pure = true)
    public static Predicate<Long> isMultipleOf(List<Long> values) {
        return isMultipleOf(values, alwaysFalse());
    }

    @NotNull
    @Contract(pure = true)
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
