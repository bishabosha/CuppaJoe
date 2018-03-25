package io.cuppajoe.collections.immutable;

import io.cuppajoe.Iterables;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func2;
import io.cuppajoe.functions.Func3;
import io.cuppajoe.math.PredicateFor;
import io.cuppajoe.patterns.Case;
import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;
import io.cuppajoe.tuples.Apply2;
import io.cuppajoe.tuples.Product2;
import io.cuppajoe.tuples.Unapply0;
import io.cuppajoe.tuples.Unapply2;
import io.cuppajoe.typeclass.applicative.Applicative1;
import io.cuppajoe.typeclass.monad.Monad1;
import io.cuppajoe.typeclass.monoid.Monoid1;
import io.cuppajoe.typeclass.value.Value1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.cuppajoe.API.*;

public interface List<E> extends Seq<List, E>, Value1<List, E> {

    /**
     * Creates a new of instance with a head and another of for a tail.
     * @param x the head of the new of.
     * @param xs the tail of the new of.
     * @param <R> the type encapsulated by the of
     * @return the new of instance
     */
    @NotNull
    @Contract(pure = true)
    static <R> Cons<R> concat(R x, List<R> xs) {
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
            return new Cons<>(null, Empty.getInstance());
        }
        List<R> list = List.empty();
        for (var x = elems.length - 1; x >= 0; x--) {
            list = new Cons<>(elems[x], list);
        }
        return list;
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
    default E get() {
        return head();
    }

    @Override
    default List<E> or(Supplier<? extends Value1<List, ? extends E>> alternative) {
        return isEmpty() ? Value1.Type.<List<E>, List, E>narrow(alternative.get()) : this;
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
        return foldRight(of(elem), List::push);
    }

    @Override
    Option<Product2<E, List<E>>> pop();

    default <U> Option<U> pop(Case<Product2<E, List<E>>, U> matcher) {
        return pop().match(matcher);
    }

    /**
     * Removes all instances of the element from the of
     * @param elem the element to remove
     * @return a new of instance with the element removed.
     */
    @Override
    default List<E> removeAll(E elem) {
        return foldLeft(empty(), (List<E> xs, E x) -> Objects.equals(x, elem) ? xs : xs.push(x)).reverse();
    }

    /**
     * Iterates through the elements of the of, passing an accumulator, the next element and the tail.
     * @param identity Supplies the accumulator that may be modified by the consumer.
     * @param consumer A function taking the accumulator, the head of the of, and the tail of the of
     * @param <O> the type of the accumulator.
     * @return The accumulator with whatever modifications have been applied.
     */
    default <O> O loop(Supplier<O> identity, Func3<O, List<E>, E, Product2<O, List<E>>> consumer) {
        Option<Product2<O, List<E>>> accStackOp = Some(Tuple(identity.get(), this));
        while (!accStackOp.isEmpty() && !accStackOp.get().$2().isEmpty()) {
            accStackOp = accStackOp.get().compose((acc, stack) ->
                stack.pop()
                     .map(headTail -> headTail.compose((head, tail) ->
                         consumer.apply(acc, tail, head))));
        }
        return accStackOp.map(Product2::$1).orElseSupply(identity);
    }

    default <O> Product2<Option<O>, List<E>> nextItem(Func2<E, List<E>, Option<Product2<Option<O>, List<E>>>> mapper) {
        Option<Product2<Option<O>, List<E>>> loopCond = Some(Tuple(Option.<O>nothing(), this));
        while (!loopCond.isEmpty() && loopCond.get().$1().isEmpty()) {
            loopCond = loopCond.get()
                               .$2()
                               .pop()
                               .flatMap(t -> t.compose(mapper));
        }
        return loopCond.orElseSupply(() -> Tuple(Nothing(), empty()));
    }

    @Override
    default List<E> reverse() {
        return foldLeft(mempty(), List::push);
    }

    @Override
    default List<E> subsequence(int from, int limit) {
        if (from < 0) {
            throw new IllegalArgumentException("from can't be less than zero.");
        } else if (limit < 0) {
            throw new IllegalArgumentException("limit can't be less than zero.");
        } else if (limit < from) {
            throw new IllegalArgumentException("limit must be greater than or equal to from.");
        }
        var it = this;
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

    private List<E> bufferElementsReversed(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit can't be less than zero.");
        }
        var it = this;
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

    @Override
    default <O> List<E> distinct(Function<E, O> propertyGetter) {
        var isDistinct = PredicateFor.distinctProperty(propertyGetter);
        return foldRight(mempty(), (xs, x) -> isDistinct.test(x) ? xs.push(x) : xs);
    }

    @Override
    default <U> List<U> pure(U value) {
        return of(value);
    }

    @Override
    default List<E> mempty() {
        return empty();
    }


    default List<E> mappend(Monoid1<List, ? extends E> other) {
        return foldRight(Monoid1.Type.<List<E>, List, E>narrow(other), List::push);
    }

    @Override
    default List<E> mconcat(List<Monoid1<List, ? extends E>> list) {
        return Monoid1.Type.narrow(Seq.super.mconcat(list));
    }

    @Override
    default <U> List<U> map(Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return !isEmpty() ? foldRight((List)List.<U>empty(), (xs, x) -> xs.push(mapper.apply(x))) : empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> List<U> flatMap(Function<? super E, Monad1<List, ? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return !isEmpty() ? List.<U>empty().mconcat(map(mapper.andThen(Monad1.Type::<List<U>, List, U>narrow))) : empty();
    }

    @Override
    default <U> List<U> apply(Applicative1<List, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    final class Empty<E> implements List<E>, Unapply0, EmptySeq<List, E> {

        /**
         * Pattern to test if any object is equivalent to an empty tail element.
         */
        public static Pattern ¥Empty() {
            return x -> x instanceof Empty<?> ? Pattern.PASS : Pattern.FAIL;
        }

        private static final Empty<?> EMPTY_LIST = new List.Empty<>();

        @SuppressWarnings("unchecked")
        private static <O> Empty<O> getInstance() {
            return (Empty<O>) EMPTY_LIST;
        }

        private Empty() {
        }

        @Override
        public Option<Product2<E, List<E>>> pop() {
            return Nothing();
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
            } else if (limit < 0) {
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
            return Iterables.empty();
        }

        @Override
        public String toString() {
            return "[]";
        }
    }

    final class Cons<E> implements List<E>, Unapply2<E, List<E>> {
        private E head;
        private List<E> tail;

        private static final Func2<Pattern, Pattern, Pattern> PATTERN = PatternFactory.gen2(Cons.class);

        /**
         * Composes patterns for the head and tail and checks that the element is a of.
         * @param $x The pattern for the head
         * @param $xs The pattern for the tail
         * @return The composed pattern
         */
        public static Pattern $Cons(Pattern $x, Pattern $xs) {
            return PATTERN.apply($x, $xs);
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

        static <O> Apply2<O, List<O>, Cons<O>> Applied() {
            return Func2.<O, List<O>, Cons<O>>of(List::concat).tupled()::apply;
        }

        @Override
        public Option<Product2<E, List<E>>> pop() {
            return Some(unapply());
        }

        @Override
        public Product2<E, List<E>> unapply() {
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

        @Override
        public <R> List<R> map(Function<? super E, ? extends R> mapper) {
            return foldRight(empty(), (List<R> xs, E x) -> xs.push(mapper.apply(x)));
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
        public <O> O foldLeft(O accumulator, BiFunction<O, E, O> mapper) {
            List<E> elements = this;
            Objects.requireNonNull(mapper);
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
        public int hashCode() {
            return foldLeft(1, (hash, x) -> 31*hash + (x == null ? 0 : Objects.hashCode(x)));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || Option.of(obj)
                .cast(Cons.class)
                .map(l -> allMatchExhaustive(l, Objects::equals))
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
            return Iterables.toString('[', ']', iterator());
        }
    }
}
