/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.immutable;

import com.bishabosha.caffeine.base.AbstractBase;
import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.*;
import com.bishabosha.caffeine.functional.functions.Func3;
import com.bishabosha.caffeine.functional.tuples.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;

/**
 * Immutable List
 * @param <E> the Type of the list
 */
public class Cons<E> extends AbstractBase<Option<E>> {
    private Option<E> head;
    private Cons<E> tail;

    /**
     * Singleton instance of the empty list
     */
    private static final Cons<?> EMPTY_LIST = new Cons<>();

    /**
     * Returns the singleton instance of the empty list
     * @param <R> the type encapsulated by the of
     */
    public static <R> Cons<R> empty() {
        return (Cons<R>) EMPTY_LIST;
    }

    /**
     * Creates a new of instance with a head and another of for a tail.
     * @param x the head of the new of.
     * @param xs the tail of the new of.
     * @param <R> the type encapsulated by the of
     * @return the new of instance
     */
    public static <R> Cons<R> concat(R x, Cons<R> xs) {
        return new Cons<>(x, xs);
    }

    /**
     * Creates a new of instance with all the elements passed.
     * @param elems the elements to add, in order of precedence from the head.
     * @param <R> the type encapsulated by the of
     * @return The new of instance.
     */
    public static <R> Cons<R> of(R... elems) {
        return fromArray(elems);
    }

    public static <R> Cons<R> fromArray(R[] elems) {
        Cons<R> cons = empty();
        if (Objects.isNull(elems)) {
            return concat(null, cons);
        }
        for (int x = elems.length - 1; x >= 0; x--) {
            cons = cons.push(elems[x]);
        }
        return cons;
    }

    /**
     * Composes patterns for the head and tail and checks that the element is a of.
     * @param $x The pattern for the head
     * @param $xs The pattern for the tail
     * @return The composed pattern
     */
    public static Pattern Cons(Pattern $x, Pattern $xs) {
        return patternFor(Cons.class).testTwo(
            Tuple($x, x -> x.head),
            Tuple($xs, xs -> xs.tail)
        );
    }

    /**
     * Pattern to test if any object is equivalent to an empty tail element.
     */
    public static final Pattern Empty() {
        return x -> x instanceof Cons<?> && ((Cons<?>)x).isEmpty() ? Pattern.PASS : Pattern.FAIL;
    }

    /**
     * @return if this of is equivalent to the empty of tail.
     */
    public boolean isEmpty() {
        return head == Option.nothing() && tail == null;
    }

    /**
     * Private constructor for the empty instance
     */
    private Cons() {
        head = Option.nothing();
        tail = null;
    }

    /**
     * Private constructor to compose a new of from head element and another of for a tail.
     * @param head The element to sit at the head.
     * @param tail The of that will act as the tail after the head.
     */
    private Cons(E head, Cons<E> tail) {
        this.head = Option.ofUnknown(head);
        this.tail = Objects.requireNonNull(tail, "tail must be a non null Cons.");
    }

    public Option<E> head() {
        return head;
    }

    public Cons<E> tail() {
        return Objects.isNull(tail) ? empty() : tail;
    }

    /**
     * Creates a new of instance with this as its tail and elem as its head.
     * @param elem the new element to push to the head.
     * @return A new of instance.
     */
    public Cons<E> push(E elem) {
        return concat(elem, this);
    }

    /**
     * Tries to split the of into a Tuple of its head and tail.
     * @return {@link Option#nothing()} if this is an empty of. Otherwise {@link Option} of a Tuple of the head and tail.
     */
    public Option<Tuple2<Option<E>, Cons<E>>> pop() {
        return when(() -> !Objects.equals(tail, empty()), () -> Tuple(head, tail)).match();
    }

    /**
     * Removes all instances of the element from the of
     * @param elem the element to remove
     * @return a new of instance with the element removed.
     */
    public Cons<E> remove(E elem) {
        Option<E> toRemove = Option.ofUnknown(elem);
        Cons<E> it = this;
        Cons<E> buffer = empty();
        while (!it.isEmpty()) {
            if (!Objects.equals(it.head, toRemove)) {
                buffer = buffer.push(it.head.orElse(null));
            }
            it = it.tail;
        }
        return buffer.reverse();
    }

    /**
     * Checks if the of contains any instances of elem.
     * @param elem the element to check for.
     * @return true if any instances were found. Otherwise false.
     */
    public boolean contains(Object elem) {
        Option<Object> option = Option.ofUnknown(elem);
        Cons<E> it = this;
        while (Objects.nonNull(it.tail)) {
            if (it.head.equals(option)) {
                return true;
            }
            it = it.tail;
        }
        return false;
    }

    /**
     * Iterates through the elements of the of, passing an accumulator, the next element and the tail.
     * @param identity Supplies the accumulator that may be modified by the consumer.
     * @param consumer A function taking the accumulator, the head of the of, and the tail of the of
     * @param <O> the type of the accumulator.
     * @return The accumulator with whatever modifications have been applied.
     */
    public <O> O loop(Supplier<O> identity, Func3<O, Cons<E>, Option<E>, Tuple2<O, Cons<E>>> consumer) {
        Tuple2<O, Cons<E>> accStackPair = Tuple(identity.get(), this);
        while (!accStackPair.$2().isEmpty()) {
            final O acc = accStackPair.$1();
            final Cons<E> stack = accStackPair.$2();
            accStackPair = stack.pop()
                                .map(t -> t.map((head, tail) -> consumer.apply(acc, tail, head)))
                                .orElse(Tuple(null, empty()));
        }
        return Option.ofUnknown(accStackPair.$1()).orElseGet(identity);
    }

    /**
     * @return the number of elements in the of
     */
    @Override
    public int size() {
        Cons<E> cons = this;
        int size = 0;
        while (Objects.nonNull(cons.tail)) {
            size = size + 1;
            cons = cons.tail;
        }
        return size;
    }

    public Cons<E> reverse() {
        Cons<E> result = Cons.empty();
        Cons<E> buffer = this;
        while (!buffer.isEmpty()) {
            result = result.push(buffer.head.orElse(null));
            buffer = buffer.tail;
        }
        return result;
    }

    public Cons<E> take(int n) {
        return takeInternal(n, "n");
    }

    private Cons<E> takeInternal(int n, String label) {
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
            buffer = buffer.push(it.head.orElse(null));
            it = it.tail;
        }
        return buffer.reverse();
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
        return it.takeInternal(to - from, "to");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ? true : Option.ofUnknown(obj)
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
                if (Objects.nonNull(cons.tail)) {
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

    public Iterator<E> iteratorFlatten() {
        return new Iterables.Lockable<E>() {

            E current = null;
            Cons<E> cons = Cons.this;

            @Override
            public boolean hasNextSupplier() {
                if (Objects.nonNull(cons.tail)) {
                    current = cons.head.orElse(null);
                    cons = cons.tail;
                    return true;
                }
                return false;
            }

            @Override
            public E nextSupplier() {
                return current;
            }
        };
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', iteratorFlatten());
    }
}
