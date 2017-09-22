/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.control.Option;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import static com.bishabosha.cuppajoe.API.Nothing;
import static com.bishabosha.cuppajoe.API.Some;
import static com.bishabosha.cuppajoe.API.Tuple;

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

    public static <T, O extends Iterable<T>> List<T> flatten(Class<O> flattenClass, O toFlatten) {
        return Cons.concat(toFlatten.iterator(), Cons.empty()).loop(
                ArrayList::new,
                (list, stack, optionIt) -> {
                    if (!optionIt.isEmpty()) {
                        final Iterator<T> it = optionIt.get();
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
