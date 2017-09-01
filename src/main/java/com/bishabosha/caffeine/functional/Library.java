/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.immutable.Cons;
import com.bishabosha.caffeine.functional.tuples.Tuple;
import com.bishabosha.caffeine.functional.tuples.Tuples;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.bishabosha.caffeine.functional.immutable.Cons.Cons;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;

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
        return Cons(toFlatten.iterator(), Cons.empty()).loop(
            ArrayList::new,
            (list, stack, it) -> {
                T current;
                Iterator<T> next;
                while (it.hasNext()) {
                    current = it.next();
                    while (flattenClass.isInstance(current) && (next = flattenClass.cast(current).iterator()).hasNext()) {
                        current = next.next();
                        stack = stack.push(next);
                    }
                    if (!flattenClass.isInstance(current)) {
                        list.add(current);
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
