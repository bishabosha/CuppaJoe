package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.typeclass.monad.Monad1;
import com.bishabosha.cuppajoe.typeclass.monoid.Monoid1;
import com.bishabosha.cuppajoe.typeclass.peek.Peek1;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Seq<INSTANCE extends Seq, E> extends Bunch<E>, Foldable<E>, Monoid1<INSTANCE, E>, Monad1<INSTANCE, E>, Peek1<E> {

    int size();
    boolean isEmpty();
    E get(int i);
    E head();
    Seq<INSTANCE, E> tail();
    Option<? extends Product2<E, ? extends Seq<INSTANCE, E>>> pop();
    Seq<INSTANCE, E> take(int limit);
    Seq<INSTANCE, E> takeRight(int limit);
    Seq<INSTANCE, E> subsequence(int from, int limit);
    Seq<INSTANCE, E> push(E elem);
    Seq<INSTANCE, E> append(E elem);
    Seq<INSTANCE, E> removeAll(E elem);

    Seq<INSTANCE, E> reverse();
    <O> Seq<INSTANCE, E> distinct(Function<E, O> propertyGetter);

    @Override
    default <O> O foldRight(O accumulator, BiFunction<O, E, O> mapper) {
        return reverse().foldLeft(accumulator, mapper);
    }

    @Override
    default void peek(Consumer<? super E> consumer) {
        forEach(consumer);
    }

    default java.util.List<E> toJavaList() {
        return foldLeft(new ArrayList<>(), (xs, x) -> {
            xs.add(x);
            return xs;
        });
    }

    default Stream<E> toJavaStream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED), false);
    }

    interface EmptySeq<INSTANCE extends Seq, E> extends Seq<INSTANCE, E> {

        default int size() {
            return 0;
        }

        default boolean isEmpty() {
            return true;
        }

        default E get(int i) throws IndexOutOfBoundsException {
            if (i < 0) {
                throw new IllegalArgumentException("Index must be positive");
            }
            throw new IndexOutOfBoundsException("List is empty");
        }

        default <O> O foldLeft(O accumulator, BiFunction<O, E, O> mapper) {
            Objects.requireNonNull(mapper);
            return accumulator;
        }

        default <O> O foldRight(O accumulator, BiFunction<O, E, O> mapper) {
            Objects.requireNonNull(mapper);
            return accumulator;
        }

        default java.util.List<E> toJavaList() {
            return Collections.emptyList();
        }

        default Stream<E> toJavaStream() {
            return Stream.empty();
        }

        default Iterator<E> iterator() {
            return Collections.emptyIterator();
        }
    }
}
