package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.applicative.Applicative1;
import com.github.bishabosha.cuppajoe.higher.foldable.Foldable;
import com.github.bishabosha.cuppajoe.higher.functions.Func2;
import com.github.bishabosha.cuppajoe.higher.functions.Func3;
import com.github.bishabosha.cuppajoe.higher.functions.TailCall;
import com.github.bishabosha.cuppajoe.higher.monad.Monad1;
import com.github.bishabosha.cuppajoe.higher.monoid.Monoid1;
import com.github.bishabosha.cuppajoe.higher.value.Value1;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.tuples.Unapply0;
import com.github.bishabosha.cuppajoe.tuples.Unapply2;
import com.github.bishabosha.cuppajoe.tuples.Unit;
import com.github.bishabosha.cuppajoe.util.Iterators;
import com.github.bishabosha.cuppajoe.util.Iterators.IdempotentIterator;
import com.github.bishabosha.cuppajoe.util.Predicates;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.*;
import static com.github.bishabosha.cuppajoe.higher.monad.Monad1.applyImpl;

public interface List<E> extends Seq<List, E>, Value1<List, E> {

    @Override
    default E head() {
        throw new NoSuchElementException();
    }

    /**
     * Creates a new of instance with a head and another of for a tail.
     *
     * @param x   the head of the new of.
     * @param xs  the tail of the new of.
     * @param <R> the type encapsulated by the of
     * @return the new of instance
     */
    static <R> Cons<R> concat(R x, List<R> xs) {
        return new Cons<>(x, xs);
    }

    /**
     * Returns the singleton instance of the empty list
     */
    static <E> List<E> empty() {
        return Monad1.Type.<List<E>, List, E>castParam(Nil.INSTANCE);
    }

    static <R> List<R> singleton(R elem) {
        return concat(elem, empty());
    }

    /**
     * Creates a new of instance with all the elements passed.
     *
     * @param elems the elements to add, in order of precedence from the head.
     * @param <R>   the type encapsulated by the of
     * @return The new of instance.
     */
    @SafeVarargs
    static <R> List<R> ofAll(R... elems) {
        if (Objects.isNull(elems)) {
            return new Cons<>(null, List.empty());
        }
        List<R> list = List.empty();
        for (var x = elems.length - 1; x >= 0; x--) {
            list = new Cons<>(elems[x], list);
        }
        return list;
    }

    /**
     * Creates a new of instance with this as its tail and elem as its head.
     *
     * @param elem the new element to push to the head.
     * @return A new of instance.
     */
    @Override
    default List<E> push(E elem) {
        return concat(elem, this);
    }

    @Override
    default E get() {
        return head();
    }

    @Override
    default List<E> or(@NonNull Supplier<? extends Value1<List, ? extends E>> alternative) {
        Objects.requireNonNull(alternative, "alternative");
        return isEmpty() ? Value1.Type.<List<E>, List, E>narrow(alternative.get()) : this;
    }

    @Override
    default List<E> tail() {
        throw new NoSuchElementException();
    }

    @Override
    default Seq<List, E> take(int limit) {
        if (limit > 0) {
            throw new IndexOutOfBoundsException("limit exceeds size.");
        } else if (limit < 0) {
            throw new IllegalArgumentException("index must be positive");
        }
        return empty();
    }

    @Override
    default List<E> takeRight(int limit) {
        return empty();
    }

    @Override
    default List<E> append(E elem) {
        return foldRight(singleton(elem), List::push);
    }

    @Override
    default Option<Tuple2<E, List<E>>> pop() {
        return None();
    }

    /**
     * Removes all instances of the element from the of
     *
     * @param elem the element to remove
     * @return a new of instance with the element removed.
     */
    @Override
    default List<E> removeAll(E elem) {
        return foldLeft(empty(), (List<E> xs, E x) -> Objects.equals(x, elem) ? xs : xs.push(x)).reverse();
    }

    /**
     * Iterates through the elements of the of, passing an accumulator, the next element and the tail.
     *
     * @param identity Supplies the accumulator that may be modified by the consumer.
     * @param consumer A function taking the accumulator, the head of the of, and the tail of the of
     * @param <O>      the type of the accumulator.
     * @return The accumulator with whatever modifications have been applied.
     */
    default <O> O loop(O identity, @NonNull Func3<E, O, List<E>, @NonNull Tuple2<O, List<E>>> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        return loopRec(Tuple(identity, this), consumer).get();
    }

    private <O> TailCall<O> loopRec(Tuple2<O, List<E>> loop, Func3<E, O, List<E>, Tuple2<O, List<E>>> consumer) {
        return loop.compose((acc, stack) ->
                stack.pop()
                     .map(headTail ->
                         headTail.compose((head, tail) ->
                             Call(() -> loopRec(consumer.apply(head, acc, tail), consumer))
                         )
                     )
                     .orElseSupply(() ->
                        Yield(acc)
                     )
        );
    }

    default <O> Option<Tuple2<O, List<E>>> nextItem(@NonNull Func2<E, List<E>, Either<List<E>, Tuple2<O, List<E>>>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return nextItemRec(Left(this), mapper).get();
    }

    private <O> TailCall<Option<Tuple2<O, List<E>>>> nextItemRec(Either<List<E>, Tuple2<O, List<E>>> loop, Func2<E, List<E>, Either<List<E>, Tuple2<O, List<E>>>> mapper) {
        return loop.transform(
                xs -> xs.pop()
                        .map(popped -> Call(() -> nextItemRec(popped.compose(mapper), mapper)))
                        .orElseSupply(() -> Yield(None())),
                result -> Yield(Some(result)));
    }

    @Override
    default List<E> reverse() {
        return foldLeft(mempty(), List::push);
    }

    @Override
    default List<E> subsequence(int from, int limit) {
        return empty();
    }

    @Override
    default <O> List<E> distinct(@NonNull Function<E, O> propertyGetter) {
        var isDistinct = Predicates.distinctProperty(propertyGetter);
        return foldRight(mempty(), (xs, x) -> isDistinct.test(x) ? xs.push(x) : xs);
    }

    @Override
    default <U> List<U> pure(U value) {
        return singleton(value);
    }

    @Override
    default List<E> mempty() {
        return empty();
    }


    default List<E> mappend(@NonNull Monoid1<List, ? extends E> other) {
        Objects.requireNonNull(other, "other");
        return foldRight(Monoid1.Type.<List<E>, List, E>narrow(other), List::push);
    }

    @Override
    default List<E> mconcat(@NonNull Foldable<Monoid1<List, ? extends E>> list) {
        return Monoid1.Type.narrow(Seq.super.mconcat(list));
    }

    @Override
    default <U> List<U> map(@NonNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return !isEmpty() ? foldRight((List) List.<U>empty(), (xs, x) -> xs.push(mapper.apply(x))) : empty();
    }

    @Override
    default <U> List<U> flatMap(@NonNull Function<? super E, Monad1<List, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return !isEmpty() ? List.<U>empty().mconcat(map(mapper.andThen(Monad1.Type::<List<U>, List, U>narrow))) : empty();
    }

    @Override
    default <U> List<U> apply(@NonNull Applicative1<List, Function<? super E, ? extends U>> applicative1) {
        return applyImpl(this, applicative1);
    }

    private List<E> bufferElementsReversed(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit can't be less than zero.");
        }
        List<E> it = this;
        List<E> buffer = List.empty();
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

    final class Cons<E> implements List<E>, Unapply2<E, List<E>> {
        private E head;
        private List<E> tail;

        /**
         * Private constructor to compose a new of from head element and another of for a tail.
         *
         * @param head The element to sit at the head.
         * @param tail The of that will act as the tail after the head.
         */
        private Cons(E head, List<E> tail) {
            Objects.requireNonNull(tail, "tail must be a non null Cons.");
            this.head = head;
            this.tail = tail;
        }

        @Override
        public Option<Tuple2<E, List<E>>> pop() {
            return Some(unapply());
        }

        @Override
        public Tuple2<E, List<E>> unapply() {
            return Tuple(head(), tail());
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

        /**
         * Checks if the of contains any instances of elem.
         *
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
        public <O> O foldLeft(O accumulator, @NonNull BiFunction<O, E, O> mapper) {
            Objects.requireNonNull(mapper, "mapper");
            List<E> elements = this;
            while (!elements.isEmpty()) {
                accumulator = mapper.apply(accumulator, elements.head());
                elements = elements.tail();
            }
            return accumulator;
        }

        @Override
        public int size() {
            List<E> list = this;
            var size = 0;
            while (!list.isEmpty()) {
                size = size + 1;
                list = list.tail();
            }
            return size;
        }

        @Override
        public E get(int n) {
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

        @Override
        public List<E> take(int limit) {
            List<E> it = this;
            return it.bufferElementsReversed(limit).reverse();
        }

        @Override
        public List<E> takeRight(int limit) {
            return reverse().bufferElementsReversed(limit);
        }

        @Override
        public List<E> subsequence(int from, int limit) {
            if (from < 0) {
                throw new IllegalArgumentException("from can't be less than zero.");
            } else if (limit < 0) {
                throw new IllegalArgumentException("limit can't be less than zero.");
            } else if (limit < from) {
                throw new IllegalArgumentException("limit must be greater than or equal to from.");
            }
            List<E> it = this;
            var count = 0;
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
        public int hashCode() {
            return foldLeft(1, (hash, x) -> 31 * hash + (x == null ? 0 : Objects.hashCode(x)));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Cons && Iterators.equals(iterator(), ((Cons) obj).iterator());
        }

        /**
         * @return an Iterator through each element of the of. The calling of is immutable.
         */
        @Override
        public Iterator<E> iterator() {
            return new IdempotentIterator<>() {

                E current = null;
                List<E> cons = Cons.this;

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
            return Iterators.toString('[', ']', iterator());
        }
    }

    enum Nil implements List<Unit>, Unapply0, EmptySeq<List, Unit> {

        INSTANCE;

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "[]";
        }
    }
}
