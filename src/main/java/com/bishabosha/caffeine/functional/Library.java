/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.immutable.Cons;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import static com.bishabosha.caffeine.functional.API.Tuple;

/**
 * Created by Jamie on 08/06/2017.
 */
public class Library {

    public static <O> Option<O> loop(BooleanSupplier condition, O acc, Function<O, Option<Boolean>> breakCondition) {
        Option<Boolean> breaker;
        while (condition.getAsBoolean()) {
            breaker = Objects.requireNonNull(breakCondition.apply(acc));
            if (breaker.isSome()) {
                if (breaker.get()) {
                    return Option.of(acc);
                }
            } else {
                return Option.nothing();
            }
        }
        return Option.nothing();
    }

    public static <T, O extends Iterable<T>> List<T> flatten(Class<O> flattenClass, O toFlatten) {
        return Cons.concat(toFlatten.iterator(), Cons.empty()).loop(
                ArrayList::new,
                (list, stack, optionIt) -> {
                    if (optionIt.isSome()) {
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
