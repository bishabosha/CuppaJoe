package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.Value;
import com.bishabosha.cuppajoe.control.Either;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.functions.Func3;
import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.tuples.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.cuppajoe.API.*;
import static com.bishabosha.cuppajoe.API.Left;
import static com.bishabosha.cuppajoe.patterns.PatternFactory.patternFor;

public interface List<E> extends Seq<E>, Unapply2<E, List<E>> {

    /**
     * Creates a new of instance with a head and another of for a tail.
     * @param x the head of the new of.
     * @param xs the tail of the new of.
     * @param <R> the type encapsulated by the of
     * @return the new of instance
     */
    @NotNull
    @Contract(pure = true)
    static <R> List<R> concat(R x, List<R> xs) {
        return new Cons<>(x, xs);
    }

    /**
     * Returns the singleton instance of the empty list
     * @param <R> the type encapsulated by the of
     */
    @Contract(pure = true)
    static <R> Empty<R> empty() {
        return Empty.getInstance();
    }

    @NotNull
    @Contract(pure = true)
    static <R> List<R> of(R elem) {
        return concat(elem, empty());
    }

    /**
     * Creates a new of instance with all the elements passed.
     * @param elems the elements to add, in order of precedence from the head.
     * @param <R> the type encapsulated by the of
     * @return The new of instance.
     */
    @SafeVarargs
    static <R> List<R> of(R... elems) {
        if (Objects.isNull(elems)) {
            return concat(null, empty());
        }
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
    @Override
    default List<E> push(E elem) {
        return concat(elem, this);
    }

    @Override
    default Option<Product2<E, List<E>>> pop() {
        return unapply();
    }

    @Override
    default E get() {
        return head();
    }

    @Override
    List<E> tail();

    @Override
    default List<E> take(int limit) {
        return bufferElementsReversed(limit).reverse();
    }

    @Override
    default List<E> takeRight(int limit) {
        return reverse().bufferElementsReversed(limit);
    }

    @Override
    default List<E> append(E elem) {
        return foldRight(of(elem), (List<E> xs, E x) -> xs.push(x));
    }

    default <U> Option<U> pop(Case<Product2<E, List<E>>, U> matcher) {
        return pop().match(matcher);
    }

    /**
     * Removes all instances of the element from the of
     * @param elem the element to remove
     * @return a new of instance with the element removed.
     */
    @Override
    default List<E> remove(E elem) {
        return fold(empty(), (List<E> xs, E x) -> Objects.equals(x, elem) ? xs : xs.push(x)).reverse();
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

    default <O> Tuple2<Option<O>, List<E>> nextItem(Func2<E, List<E>, Tuple2<Either<Boolean, O>, List<E>>> mapper) {
        Tuple2<Either<Boolean, O>, List<E>> loopCond = Tuple(Left(true), this);
        while (loopCond.$1().getLeftOrElse(() -> false)) {
            loopCond = loopCond.$2().pop()
                .map(t -> t.map(mapper))
                .orElseGet(() -> Tuple(Left(false), empty()));
        }
        return loopCond.flatMap((either, cons) -> Tuple(either.maybeRight(), cons));
    }

    @Override
    default int size() {
        List<E> list = this;
        int size = 0;
        while (!list.isEmpty()) {
            size = size + 1;
            list = list.tail();
        }
        return size;
    }

    @Override
    default List<E> reverse() {
        List<E> result = empty();
        List<E> buffer = this;
        while (!buffer.isEmpty()) {
            result = result.push(buffer.head());
            buffer = buffer.tail();
        }
        return result;
    }

    @Override
    default List<E> subsequence(int from, int limit) {
        if (from < 0) {
            throw new IllegalArgumentException("from can't be less than zero.");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit can't be less than zero.");
        }
        if (limit < from) {
            throw new IllegalArgumentException("limit must be greater than or equal to from.");
        }
        List<E> it = this;
        int count = 0;
        while (count < from) {
            count = count + 1;
            if (it.isEmpty()) {
                throw new IndexOutOfBoundsException("from is larger than size");
            }
            it = it.tail();
        }
        return it.bufferElementsReversed(limit - from).reverse();
    }

    @Override
    default E get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be positive.");
        }
        List<E> it = this;
        while (n >= 0) {
            if (it.isEmpty()) {
                break;
            }
            if (n == 0) {
                return it.head();
            }
            n = n - 1;
            it = it.tail();
        }
        throw new IndexOutOfBoundsException("n exceeds size.");
    }

    private List<E> bufferElementsReversed(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit can't be less than zero.");
        }
        List<E> it = this;
        List<E> buffer = empty();
        while (limit > 0) {
            limit = limit - 1;
            if (it.isEmpty()) {
                throw new IndexOutOfBoundsException("limit exceeds size.");
            }
            buffer = buffer.push(it.head());
            it = it.tail();
        }
        return buffer;
    }

    @Override
    default Option<Product2<E, List<E>>> unapply() {
        return isEmpty() ? Nothing() : Some(Tuple(head(), tail()));
    }

    static <O> Apply2<O, List<O>, Cons<O>> Applied() {
        return t -> new Cons<>(t.$1(), t.$2());
    }

    class Empty<E> implements List<E>, Apply0<Empty<E>> {

        /**
         * Pattern to test if any object is equivalent to an empty tail element.
         */
        public static Pattern Empty() {
            return x -> x instanceof Empty<?> ? Pattern.PASS : Pattern.FAIL;
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
        public List<E> take(int limit) {
            if (limit > 0) {
                throw new IndexOutOfBoundsException("limit exceeds size.");
            }
            if (limit < 0) {
                throw new IllegalArgumentException("index must be positive");
            }
            return empty();
        }

        @Override
        public List<E> takeRight(int limit) {
            return take(limit);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(E obj) {
            return false;
        }

        @Override
        public <R> List<R> map(@NotNull Function<? super E, ? extends R> mapper) {
            return empty();
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return Iterables.emptyIterator();
        }

        @Override
        public String toString() {
            return "[]";
        }

        @Override
        public Empty<E> apply(Product0 tuple) {
            return getInstance();
        }
    }

    class Cons<E> implements List<E> {
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
        public boolean isEmpty() {
            return false;
        }

        @Override
        public <R> Value<R> map(Function<? super E, ? extends R> mapper) {
            return foldRight(empty(), (List<R> xs, E x) -> xs.push(mapper.apply(x)));
        }

        /**
         * Checks if the of contains any instances of elem.
         * @param elem the element to check for.
         * @return true if any instances were found. Otherwise false.
         */
        public boolean contains(Object elem) {
            Seq<E> list = this;
            while (!list.isEmpty()) {
                if (Objects.equals(list.head(), elem)) {
                    return true;
                }
                list = list.tail();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return foldLeft(1, (hash, x) -> 31*hash + (x == null ? 0 : x.hashCode()));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this ? true : Option.of(obj)
                    .cast(Cons.class)
                    .map(l -> allMatch(l, Objects::equals))
                    .orElse(false);
        }

        /**
         * @return an Iterator through each element of the of. The calling of is immutable.
         */
        @Override
        @NotNull
        public Iterator<E> iterator() {
            return new Iterables.Lockable<>() {

                E current = null;
                Seq<E> cons = Cons.this;

                @Override
                public boolean hasNextSupplier() {
                    if (!cons.isEmpty()) {
                        current = cons.head();
                        cons = cons.tail();
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
            return Iterables.toString('[', ']', iterator());
        }
    }
}
