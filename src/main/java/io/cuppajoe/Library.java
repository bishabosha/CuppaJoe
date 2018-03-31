/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func2;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.cuppajoe.API.*;

public final class Library {

    public static <O> Option<O> loop(BooleanSupplier condition, O acc, Function<O, Option<Boolean>> breakCondition) {
        Option<Boolean> breaker;
        while (condition.getAsBoolean()) {
            breaker = Objects.requireNonNull(breakCondition.apply(acc));
            if (!breaker.isEmpty() && breaker.get()) {
                return Some(acc);
            } else {
                return None();
            }
        }
        return None();
    }

    /**
     * A depth first search algorithm using any nested
     */
    public static <A, O extends Iterable> A foldLeft(Class<O> branchClass, O tree, Supplier<A> accumulator, Func2<A, Object, A> mapper) {
        return List(tree.iterator()).loop(
            accumulator,
            (acc, stack, it) -> {
                if (Objects.nonNull(it)) {
                    final Object current;
                    if (it.hasNext()) {
                        current = it.next();
                        stack = stack.push(it);
                        if (!branchClass.isInstance(current)) {
                            acc = mapper.apply(acc, current);
                        } else {
                            stack = stack.push(branchClass.cast(current).iterator());
                        }
                    }
                }
                return Tuple(acc, stack);
            }
        );
    }
}
