/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.collections.immutable.List;
import com.bishabosha.cuppajoe.control.Option;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import static com.bishabosha.cuppajoe.API.*;

public final class Library {

    public static <O> Option<O> loop(BooleanSupplier condition, O acc, Function<O, Option<Boolean>> breakCondition) {
        Option<Boolean> breaker;
        while (condition.getAsBoolean()) {
            breaker = Objects.requireNonNull(breakCondition.apply(acc));
            if (!breaker.isEmpty() && breaker.get()) {
                return Some(acc);
            } else {
                return Nothing();
            }
        }
        return Nothing();
    }

    public static <T, O extends Iterable<T>> java.util.List<T> flatten(Class<O> flattenClass, O toFlatten) {
        return List.concat(toFlatten.iterator(), List()).loop(
                ArrayList::new,
                (list, stack, it) -> {
                    if (!Objects.nonNull(it)) {
                        final T current;
                        if (it.hasNext()) {
                            current = it.next();
                            stack = stack.push(it);
                            if (!flattenClass.isInstance(current)) {
                                list.add(current);
                            } else {
                                stack = stack.push(flattenClass.cast(current).iterator());
                            }
                        }
                    }
                    return Tuple(list, stack);
                });
    }

    public static boolean anyEquals(Object obj, Object... others) {
        for (Object o: others) {
            if (Objects.equals(obj, o)) {
                return true;
            }
        }
        return false;
    }

    public static boolean allEqual(Object obj, Object... others) {
        for (Object o: others) {
            if (!Objects.equals(obj, o)) {
                return false;
            }
        }
        return true;
    }
}
