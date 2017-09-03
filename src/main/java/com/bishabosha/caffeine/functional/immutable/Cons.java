/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.immutable;

import com.bishabosha.caffeine.base.AbstractBase;
import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.*;
import com.bishabosha.caffeine.functional.functions.Consume3;
import com.bishabosha.caffeine.functional.functions.Func3;
import com.bishabosha.caffeine.functional.tuples.Tuple;
import com.bishabosha.caffeine.functional.tuples.Tuple2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;

/**
 * Immutable List
 * @param <E> the Type of the list
 */
public class Cons<E> extends AbstractBase<E> {
    private E head;
    private Cons<E> tail;

    /**
     * Singleton instance of the empty list
     */
    private static final Cons<?> EMPTY_LIST = new Cons<>();

    /**
     * Returns the singleton instance of the empty list
     * @param <R> the type encapsulated by the Cons
     */
    public static <R> Cons<R> empty() {
        return (Cons<R>) EMPTY_LIST;
    }

    /**
     * Creates a new Cons instance with a head and another Cons for a tail.
     * @param x the head of the new Cons.
     * @param xs the tail of the new Cons.
     * @param <R> the type encapsulated by the Cons
     * @return the new Cons instance
     */
    public static <R> Cons<R> Cons(R x, Cons<R> xs) {
        return new Cons<>(x, xs);
    }

    /**
     * Creates a new Cons instance with all the elements passed.
     * @param elems the elements to add, in order of precedence from the head.
     * @param <R> the type encapsulated by the Cons
     * @return The new Cons instance.
     */
    public static <R> Cons<R> of(R... elems) {
        return fromArray(elems);
    }

    public static <R> Cons<R> fromArray(R[] elems) {
        Cons<R> cons = empty();
        for (int x = elems.length - 1; x >= 0; x--) {
            cons = cons.push(elems[x]);
        }
        return cons;
    }

    /**
     * Pattern to test if any object is equivalent to an empty tail element.
     */
    public static final Pattern ¥empty = x -> x instanceof Cons<?> && ((Cons<?>)x).isEmpty() ? Pattern.PASS : Pattern.FAIL;

    /**
     * Composes patterns for the head and tail and checks that the element is a Cons.
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
     * @return if this Cons is equivalent to the empty Cons tail.
     */
    public boolean isEmpty() {
        return head == null && tail == null;
    }

    /**
     * For use after {@link Cons#¥empty}, binds the head and tail without checks for null.
     */
    public static final Pattern $x_$xs = splitBase(c -> Pattern.bind(c.head, c.tail));

    /**
     * For use after {@link Cons#¥empty}, binds the tail without checks for null.
     */
    public static final Pattern ¥x_$xs = splitBase(c -> Pattern.bind(c.tail));

    /**
     * For use after {@link Cons#¥empty}, binds the head without checks for null.
     */
    public static final Pattern $x_¥xs = splitBase(c -> Pattern.bind(c.head));

    /**
     * For use after {@link Cons#¥empty}, binds the first two elements and the tail without checks for null.
     */
    public static final Pattern $x_$y_$xs = x -> Option.of(x)
                                                       .cast(Cons.class)
                                                       .filter(c -> !c.isEmpty() && !c.tail.isEmpty())
                                                       .flatMap(c -> Pattern.bind(c.head, c.tail.head, c.tail.tail));

    /**
     * Contains common code for casting to Cons and checking that the Cons isn't empty.
     * @param func the binding function to bind the pattern.
     * @return the composed pattern.
     */
    private static final Pattern splitBase(Function<Cons, Option<PatternResult>> func) {
        return x -> Option.of(x)
                          .cast(Cons.class)
                          .filter(c -> !EMPTY_LIST.equals(c))
                          .flatMap(func);
    }

    /**
     * Private constructor for the empty instance
     */
    private Cons() {
        head = null;
        tail = null;
    }

    /**
     * Private constructor to compose a new Cons from head element and another Cons for a tail.
     * @param head The element to sit at the head.
     * @param tail The Cons that will act as the tail after the head.
     */
    private Cons(E head, Cons<E> tail) {
        this.head = Objects.requireNonNull(head);
        this.tail = Objects.requireNonNull(tail);
    }

    /**
     * Tries to get the head of this Cons
     * @return {@link Option#nothing()} if this is an empty Cons. Otherwise {@link Option} of the head.
     */
    public Option<E> head() {
        return Option.ofUnknown(head);
    }

    /**
     * Tries to get the tail of this Cons
     * @return {@link Option#nothing()} if this is an empty Cons. Otherwise {@link Option} of the tail.
     */
    public Option<Cons<E>> tail() {
        return Option.ofUnknown(tail);
    }

    /**
     * Creates a new Cons instance with this as its tail and elem as its head.
     * @param elem the new element to push to the head.
     * @return A new Cons instance.
     */
    public Cons<E> push(E elem) {
        return new Cons<>(elem, this);
    }

    /**
     * Tries to split the Cons into a Tuple of its head and tail.
     * @return {@link Option#nothing()} if this is an empty Cons. Otherwise {@link Option} of a Tuple of the head and tail.
     */
    public Option<Tuple2<E, Cons<E>>> pop() {
        return when(() -> !this.isEmpty(), () -> Tuple(head, tail)).match();
    }

    /**
     * Removes all instances of the element from the Cons
     * @param elem the element to remove
     * @return a new Cons instance with the element removed.
     */
    public Cons<E> remove(E elem) {
        Objects.requireNonNull(elem);
        return match(this).of(
            with(¥empty,                   () -> empty()),
            with($x_$xs, (E $x, Cons<E> $xs) -> guardUnsafe(
                when(() -> elem.equals($x), () -> $xs.remove(elem)),
                edge(                       () -> Cons($x, $xs.remove(elem)))
            ))
        );
    }

    /**
     * Checks if the Cons contains any instances of elem.
     * @param elem the element to check for.
     * @return true if any instances were found. Otherwise false.
     */
    public boolean contains(Object elem) {
        Objects.requireNonNull(elem);
        return match(this).of(
            with(¥empty,                    () -> false),
            with($x_$xs,  (E $x, Cons<E> $xs) -> guardUnsafe(
                when(() -> elem.equals($x), () -> true),
                edge(                       () -> $xs.contains(elem))
            ))
        );
    }

    /**
     * Iterates through the elements of the Cons, passing an accumulator, the next element and the tail.
     * @param identity Supplies the accumulator that may be modified by the consumer.
     * @param consumer A function taking the accumulator, the head of the Cons, and the tail of the Cons
     * @param <O> the type of the accumulator.
     * @return The accumulator with whatever modifications have been applied.
     */
    public <O> O loop(Supplier<O> identity, Func3<O, Cons<E>, E, Tuple2<O, Cons<E>>> consumer) {
        Tuple2<O, Cons<E>> accStackPair = Tuple(identity.get(), this);
        while (!accStackPair.$2().isEmpty()) {
            final O acc = accStackPair.$1();
            final Cons<E> stack = accStackPair.$2();
            accStackPair = stack.pop()
                                .map(t -> t.map((head, tail) -> consumer.apply(acc, tail, head)))
                                .orElse(Tuple(null, Cons.empty()));
        }
        return Option.ofUnknown(accStackPair.$1()).orElseGet(identity);
    }

    /**
     * @return the number of elements in the Cons
     */
    @Override
    public int size() {
        return sizeRec(0);
    }

    /**
     * Recurse through the cons, adding 1 to the size when another element is encountered.
     * @param acc the accumulating size
     * @return the total size of this Cons
     * @throws StackOverflowError when there are 850+ elements
     */
    private int sizeRec(int acc) {
        return match(this).of(
            with(¥empty,             () -> acc),
            with(¥x_$xs, (Cons<E> $xs) -> $xs.sizeRec(acc + 1))
        );
    }

    /**
     * Takes the first n elements of the Cons
     * @param n the number of elements to take.
     * @return A new Cons instance containing the first n elements.
     * @throws StackOverflowError when n >= 408
     */
    public Cons<E> take(int n) {
        return Try.<Cons>of(() -> guardUnsafe(
            when(() -> n == 0, () -> empty()),
            thro(() -> n < 0,  () -> new IndexOutOfBoundsException("Index must be positive.")),
            when(() -> n > 0,  () -> match(this).of(
                with($x_$xs, (E $x, Cons<E> $xs) -> Cons($x, $xs.take(n - 1))),
                thro(¥empty,                  () -> new IndexOutOfBoundsException("Index given is out of bounds."))))
        ))
        .getOrThrowMessage();
    }

    /**
     * Returns any subsequence of elements in the Cons.
     * @param from the start index
     * @param to the end index
     * @return A new Cons instance containing the elements in the range given.
     * @throws RuntimeException if to is smaller than from,
     * or either index is negative, or exceeds the length of the Cons.
     * @throws StackOverflowError when sequence length >= 408
     */
    public Cons<E> subsequence(int from, int to) {
        return Try.of(() -> guardUnsafe(
            thro(() -> to < from, () -> new IllegalArgumentException("to must be larger than from")),
            thro(() -> from < 0 || to < 0, () -> new IndexOutOfBoundsException("Indexes must be positive.")),
            edge(() -> subsequenceRec(from, to-from+1))
        ))
        .getOrThrowMessage();
    }

    /**
     * Recursive implementation of subsequence.
     * @param from will reduce until the first index is found.
     * @param n the number of elements to take once the first index is found.
     * @return the new Cons instance.
     */
    private Cons<E> subsequenceRec(int from, int n) {
        return guardUnsafe(
            when(() -> from == 0, () -> take(n)),
            when(() -> from > 0,  () -> match(this).of(
                with(¥x_$xs, (Cons<E> $xs) -> $xs.subsequenceRec(from-1, n)),
                thro(¥empty, () -> new IndexOutOfBoundsException("Index given is out of bounds."))))
        );
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ? true : Option.ofUnknown(obj)
                                          .cast(Cons.class)
                                          .map(this::equalsIterative)
                                          .orElse(false);
    }

    /**
     * Recursively checks that two Cons are equal
     * @param cons the Cons to check
     * @return true, if they are the same length and all elements are equal and in the same order.
     * @throws StackOverflowError when there are 487+ elements in both
     */
    private boolean equalsRec(Cons cons) {
        return match(this).of(
            with(¥empty, () -> cons.isEmpty()),
            with($x_$xs, (E $x, Cons<E> $xs) -> guardUnsafe(
                when(() -> $x.equals(cons.head), () -> cons.tail.equalsRec($xs)),
                edge(                            () -> false)
            ))
        );
    }

    /**
     * Iteratively checks that two Cons are equal, without Stack Overflow
     * @param otherCons the Cons to check
     * @return true, if they are the same length and all elements are equal and in the same order.
     */
    private boolean equalsIterative(Cons otherCons) {
        Option<Tuple2<E, Cons<E>>> thisPopped = this.pop();
        Option<? extends Tuple2<?, ? extends Cons<?>>> otherPopped = otherCons.pop();
        while (true) {
            if (otherPopped.isEmpty() ^ thisPopped.isEmpty()) {
                return false; // if both different return false.
            }
            if (otherPopped.isEmpty() && thisPopped.isEmpty()) {
                return true;
            }
            final Tuple2<E, Cons<E>> thisTup = thisPopped.get();
            final Tuple2<?, ? extends Cons<?>> otherTup = otherPopped.get();
            if (!Objects.equals(thisTup.$1(), otherTup.$1())) {
                return false;
            }
            thisPopped = thisTup.$2().pop();
            otherPopped = otherTup.$2().pop();
        }
    }

    /**
     * @return an Iterator through each element of the Cons. The calling Cons is immutable.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterables.Lockable<E>() {

            E current = null;
            Cons<E> cons = Cons.this;
            Option<Tuple2<E, Cons<E>>> popped = Option.nothing();

            @Override
            public boolean hasNextSupplier() {
                if ((popped = cons.pop()).isSome()) {
                    cons = popped.get().$2();
                    current = popped.get().$1();
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
}
