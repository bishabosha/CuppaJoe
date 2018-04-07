package com.github.bishabosha.cuppajoe.match.internal;

import com.github.bishabosha.cuppajoe.API;
import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.Func0;
import com.github.bishabosha.cuppajoe.match.Guard;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public final class GuardFactory {

    private GuardFactory() {
    }

    /**
     * Guard that lazily evaluates a condition, and if true, will lazily supply the result given
     *
     * @param test          Supply any test to check, for example comparing for equality
     * @param valueSupplier The value to supply if the test is a success.
     * @param <O>           The type of the output variable
     * @return A Guard that will supply {@link API#None()} if the test fails. Otherwise {@link Option} of the supplied variable
     */
    public static <O> Guard<O> when(@NonNull BooleanSupplier test, @NonNull Func0<O> valueSupplier) {
        return () -> Option.when(test, valueSupplier);
    }

    /**
     * Guard that will lazily wrap the value supplied in an Option.<br>
     * Used as a catch all result in a guard block.
     *
     * @param valueSupplier supplies the value given if this edge guard is evaluated
     * @param <O>           The type of the output variable
     * @return a Guard that will supply {@link Option} of the supplied value.
     */
    public static <O> Guard<O> edge(Supplier<O> valueSupplier) {
        return () -> Some(valueSupplier.get());
    }

    @SafeVarargs
    public static <O> Guard<O> combine(Guard<O>... guards) {
        return () -> {
            Option<O> result = None();
            for (var guard : guards) {
                result = guard.match();
                if (!result.isEmpty()) {
                    break;
                }
            }
            return result;
        };
    }
}
