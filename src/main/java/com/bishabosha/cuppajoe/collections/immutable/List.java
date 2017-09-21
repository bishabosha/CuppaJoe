package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.API;
import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.functions.Func3;
import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.tuples.Applied2;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.cuppajoe.API.Nothing;
import static com.bishabosha.cuppajoe.API.Some;
import static com.bishabosha.cuppajoe.API.Tuple;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;

public interface List<E> extends Bunch<E>, Foldable<E> {

    /**
     * Returns the singleton instance of the empty list
     * @param <R> the type encapsulated by the of
     */
    @Contract(pure = true)
    static <R> List<R> empty() {
        return Empty.getInstance();
    }

    @NotNull
    @Contract(pure = true)
    static <R> List<R> of(R elem) {
        return Cons.of(elem, empty());
    }

    /**
     * Creates a new of instance with all the elements passed.
     * @param elems the elements to add, in order of precedence from the head.
     * @param <R> the type encapsulated by the of
     * @return The new of instance.
     */
    @SafeVarargs
    static <R> List<R> of(R... elems) {
        List<R> cons = empty();
        for (int x = elems.length - 1; x >= 0; x--) {
            cons = cons.push(elems[x]);
        }
        return cons;
    }

    /**
     * Creates a new of instance with this as its tail and elem as its head.
     * @param elem the new element to push to the head.
     * @return A new of instance.
     */
    default List<E> push(E elem) {
        return Cons.of(elem, this);
    }

    Option<? extends Product2<E, List<E>>> pop();

    E head();

    List<E> tail();

    default List<E> append(E elem) {
        return foldRight(of(elem), (x, xs) -> xs.push(x));
    }

    default <U> Option<U> pop(Case<Product2<E, List<E>>, U> matcher) {
        return pop().match(matcher);
    }

    /**
     * Removes all instances of the element from the of
     * @param elem the element to remove
     * @return a new of instance with the element removed.
     */
    default List<E> remove(E elem) {
        final Option<E> toRemove = Option.of(elem);
        return fold(empty(), (x, xs) -> Objects.equals(x, toRemove) ? xs : xs.push(x)).reverse();
    }

    @Override
    default <A> A foldRight(A accumulator, Func2<E, A, A> mapper) {
        return reverse().fold(accumulator, mapper);
    }

    /**
     * Iterates through the elements of the of, passing an accumulator, the next element and the tail.
     * @param identity Supplies the accumulator that may be modified by the consumer.
     * @param consumer A function taking the accumulator, the head of the of, and the tail of the of
     * @param <O> the type of the accumulator.
     * @return The accumulator with whatever modifications have been applied.
     */
    default <O> O loop(Supplier<O> identity, Func3<O, List<E>, E, Tuple2<O, List<E>>> consumer) {
        Option<Tuple2<O, List<E>>> option = Some(Tuple(identity.get(), this));
        Tuple2<O, List<E>> accStackPair;
        while (!option.isEmpty() && !(accStackPair = option.get()).$2().isEmpty()) {
            final O acc = accStackPair.$1();
            final List<E> stack = accStackPair.$2();
            option = stack.pop()
                          .map(t -> t.map((head, tail) -> consumer.apply(acc, tail, head)));
        }
        return option.map(Tuple2::$1).orElseGet(identity);
    }

    default java.util.List<E> toJavaList() {
        return fold(new ArrayList<>(), (x, xs) -> {
            xs.add(x);
            return xs;
        });
    }

    class Empty<E> implements List<E> {

        /**
         * Pattern to test if any object is equivalent to an empty tail element.
         */
        public static Pattern Empty() {
            return x -> x instanceof Cons<?> && ((Cons<?>)x).isEmpty() ? Pattern.PASS : Pattern.FAIL;
        }

        private static final Empty<?> EMPTY_LIST = new Empty<>();

        @SuppressWarnings("unchecked")
        static <O> Empty<O> getInstance() {
            return (Empty<O>) EMPTY_LIST;
        }

        private Empty() {
        }

        @Override
        public E head() {
            throw new NoSuchElementException();
        }

        @Override
        public List<E> tail() {
            throw new NoSuchElementException();
        }

        @Override
        public Option<? extends Product2<E, List<E>>> pop() {
            return Nothing();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(E obj) {
            return false;
        }
    }

    class Cons<E> implements List<E>, Applied2<E, List<E>, List<E>> {
        private E head;
        private List<E> tail;

        /**
         * Composes patterns for the head and tail and checks that the element is a of.
         * @param $x The pattern for the head
         * @param $xs The pattern for the tail
         * @return The composed pattern
         */
        public static Pattern Cons(Pattern $x, Pattern $xs) {
            return patternFor(Cons.class).testTwo(
                Tuple($x, Cons::head),
                Tuple($xs, Cons::tail)
            );
        }

        /**
         * Creates a new of instance with a head and another of for a tail.
         * @param x the head of the new of.
         * @param xs the tail of the new of.
         * @param <R> the type encapsulated by the of
         * @return the new of instance
         */
        @NotNull
        @Contract(pure = true)
        public static <R> Cons<R> of(R x, List<R> xs) {
            return new Cons<>(x, xs);
        }

        /**
         * Private constructor to compose a new of from head element and another of for a tail.
         * @param head The element to sit at the head.
         * @param tail The of that will act as the tail after the head.
         */
        private Cons(E head, List<E> tail) {
            Objects.requireNonNull(tail, "tail must be a non null Cons.");
            this.head = head;
            this.tail = tail;
        }

        @Override
        public E head() {
            return head;
        }

        @Override
        public List<E> tail() {
            return tail;
        }

        @Override
        public Option<? extends Product2<E, List<E>>> pop() {
            return Some(unapply());
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        /**
         * Checks if the of contains any instances of elem.
         * @param elem the element to check for.
         * @return true if any instances were found. Otherwise false.
         */
        public boolean contains(Object elem) {
            List<E> list = this;
            while (!list.isEmpty()) {
                if (Objects.equals(list.head(), elem)) {
                    return true;
                }
                list = list.tail();
            }
            return false;
        }

        @Override
        public List<E> apply(Product2<E, List<E>> tuple) {
            return of(tuple.$1(), tuple.$2());
        }

        @Override
        public Product2<E, List<E>> unapply() {
            return Tuple(head(), tail());
        }
    }
}
