/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.collections.immutable.List;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

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


    public static <T, O extends Iterable<T>> java.util.List<T> foldLeft(Class<O> flattenClass, O toFlatten) {
        return foldLeft(flattenClass, toFlatten, ArrayList::new, (xs, x) -> {
            xs.add(x);
            return xs;
        });
    }

    /**
     * An efficient form of {@link Library#inOrder(Class, Iterable)} that accumulates a result
     */
    public static <A, T, O extends Iterable<T>> A foldLeft(Class<O> branchClass, O tree, Supplier<A> accumulator, Func2<A, T, A> mapper) {
        return List(tree.iterator()).loop(
            accumulator,
            (acc, stack, it) -> {
                if (Objects.nonNull(it)) {
                    final T current;
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

    /**
     * Iterable that can process any Iterable object as an n-ary tree.
     * This produces an in-order traversal;
     * branching when an instance of the branchClass is found.
     * @param branchClass The class to act as an empty node, where each element is a branch
     * @param tree the object being traversed
     * @param <T> The type of elements being traversed
     * @param <O> An iterable over T
     * @return an Iterator that produces elements in order.
     */
    public static <T, O extends Iterable<T>> Iterable<T> inOrder(Class<O> branchClass, O tree) {
        return () -> new Iterables.Lockable<>() {

            private List<Iterator<T>> stack = List(tree.iterator());
            private T toReturn;

            @Override
            public boolean hasNextSupplier() {
                Option<Boolean> condition;
                while (true) {
                    condition = stack.pop()
                                     .map(t -> t.map(this::processPopped));
                    if (condition.isEmpty()) {
                        return false;
                    } else if (condition.get()) {
                        return true;
                    }
                }
            }

            private boolean processPopped(Iterator<T> it, List<Iterator<T>> xs) {
                boolean result = false;
                if (it.hasNext()) {
                    toReturn = it.next();
                    xs = xs.push(it);
                    if (!branchClass.isInstance(toReturn)) {
                        result = true;
                    } else {
                        xs = xs.push(branchClass.cast(toReturn).iterator());
                    }
                }
                stack = xs;
                return result;
            }

            @Override
            public T nextSupplier() {
                return toReturn;
            }
        };
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
