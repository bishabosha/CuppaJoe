/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Either;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Nothing;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.tuples.Applied2;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.bishabosha.cuppajoe.API.Left;
import static com.bishabosha.cuppajoe.API.Option;
import static com.bishabosha.cuppajoe.patterns.Case.*;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;
import static com.bishabosha.cuppajoe.API.Tuple;

/**
 * Immutable List
 * @param <E> the Type of the list
 */
public class Cons<E> implements List<E>, Foldable<Option<E>>, Applied2<E, Cons<E>, Cons<E>> {



    public <O> Tuple2<Option<O>, Cons<E>> nextItem(Func2<Option<E>, Cons<E>, Tuple2<Either<Boolean, O>, Cons<E>>> mapper) {
        Tuple2<Either<Boolean, O>, Cons<E>> loopCond = Tuple(Left(true), this);
        while (loopCond.$1().getLeftOrElse(() -> false)) {
            loopCond = loopCond.$2().pop()
                    .map(t -> t.map(mapper))
                    .orElseGet(() -> Tuple(Left(false), Cons.empty()));
        }
        return loopCond.flatMap((either, cons) -> Tuple(either.maybeRight(), cons));
    }

    /**
     * @return the number of elements in the of
     */
    public int size() {
        Cons<E> cons = this;
        int size = 0;
        while (!cons.isEmpty()) {
            size = size + 1;
            cons = cons.tail;
        }
        return size;
    }

    public Option<E> get() {
        return head();
    }

    public Cons<E> reverse() {
        Cons<E> result = Cons.empty();
        Cons<E> buffer = this;
        while (!buffer.isEmpty()) {
            result = result.pushOpt(buffer.head);
            buffer = buffer.tail;
        }
        return result;
    }

    public Cons<E> take(int n) {
        return takeInternalResultBackwards(n, "n").reverse();
    }

    public Cons<E> takeRight(int n) {
        return reverse().takeInternalResultBackwards(n, "n");
    }

    private Cons<E> takeInternalResultBackwards(int n, String label) {
        if (n < 0) {
            throw new IllegalArgumentException(label + " can't be less than zero.");
        }
        Cons<E> it = this;
        Cons<E> buffer = empty();
        while (n > 0) {
            n = n - 1;
            if (it.isEmpty()) {
                throw new IndexOutOfBoundsException(label + " exceeds size.");
            }
            buffer = buffer.pushOpt(it.head);
            it = it.tail;
        }
        return buffer;
    }

    public Cons<E> subsequence(int from, int to) {
        if (from < 0) {
            throw new IllegalArgumentException("from can't be less than zero.");
        }
        if (to < 0) {
            throw new IllegalArgumentException("to can't be less than zero.");
        }
        if (to < from) {
            throw new IllegalArgumentException("to must be greater than or equal to from.");
        }
        Cons<E> it = this;
        int count = 0;
        while (count < from) {
            count = count + 1;
            if (it.isEmpty()) {
                throw new IndexOutOfBoundsException("from is larger than size");
            }
            it = it.tail;
        }
        return it.takeInternalResultBackwards(to - from, "to").reverse();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ? true : Option.of(obj)
                                          .cast(Cons.class)
                                          .map(this::equateCons)
                                          .orElse(false);
    }

    /**
     * Iteratively checks that two of are equal, without Stack Overflow
     * @param otherCons the of to check
     * @return true, if they are the same length and all elements are equal and in the same order.
     */
    private boolean equateCons(Cons otherCons) {
        Option<Tuple2<Option<E>, Cons<E>>> thisPopped = this.pop();
        Option<Tuple2<Option, Cons>> otherPopped = otherCons.pop();
        while (true) {
            final boolean thisEmpty = thisPopped.isEmpty();
            final boolean otherEmpty = otherPopped.isEmpty();
            if (otherEmpty ^ thisEmpty) {
                return false; // if both different return false.
            }
            if (otherEmpty && thisEmpty) {
                return true;
            }
            final Tuple2<Option<E>, Cons<E>> thisTup = thisPopped.get();
            final Tuple2<Option, Cons> otherTup = otherPopped.get();
            if (!Objects.equals(thisTup.$1(), otherTup.$1())) {
                return false;
            }
            thisPopped = thisTup.$2().pop();
            otherPopped = otherTup.$2().pop();
        }
    }

    /**
     * @return an Iterator through each element of the of. The calling of is immutable.
     */
    @Override
    public Iterator<Option<E>> iterator() {
        return new Iterables.Lockable<Option<E>>() {

            Option<E> current = null;
            Cons<E> cons = Cons.this;

            @Override
            public boolean hasNextSupplier() {
                if (!cons.isEmpty()) {
                    current = cons.head;
                    cons = cons.tail;
                    return true;
                }
                return false;
            }

            @Override
            public Option<E> nextSupplier() {
                return current;
            }
        };
    }

    public Iterable<E> flatten() {
        return () -> new Iterator<E>() {

            Iterator<Option<E>> it = iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                return it.next().orElse(null);
            }
        };
    }

    @Override
    @NotNull
    @Contract(pure = true)
    public Cons<E> apply(Product2<E, Cons<E>> tuple) {
        return concat(tuple.$1(), tuple.$2());
    }

    @Override
    @NotNull
    @Contract(pure = true)
    public Product2<E, Cons<E>> unapply() {
        return Tuple(head.orElse(null), tail);
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', flatten().iterator());
    }
}
