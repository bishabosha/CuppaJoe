package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.typeclass.foldable.Foldable;
import com.github.bishabosha.cuppajoe.typeclass.monad.Monad1;
import com.github.bishabosha.cuppajoe.typeclass.monoid.Monoid1;
import com.github.bishabosha.cuppajoe.typeclass.peek.Peek1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
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

    Option<? extends Tuple2<E, ? extends Seq<INSTANCE, E>>> pop();

    Seq<INSTANCE, E> take(int limit);

    Seq<INSTANCE, E> takeRight(int limit);

    Seq<INSTANCE, E> subsequence(int from, int limit);

    Seq<INSTANCE, E> push(E elem);

    Seq<INSTANCE, E> append(E elem);

    Seq<INSTANCE, E> removeAll(E elem);

    Seq<INSTANCE, E> reverse();

    <O> Seq<INSTANCE, E> distinct(Function<E, O> propertyGetter);

    @Override
    default <O> O foldRight(O accumulator, @NonNull BiFunction<O, E, O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return reverse().foldLeft(accumulator, mapper);
    }

    @Override
    default void peek(@NonNull Consumer<? super E> consumer) {
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

        @Override
        default boolean contains(E obj) {
            return false;
        }

        default E get(int i) throws IndexOutOfBoundsException {
            if (i < 0) {
                throw new IllegalArgumentException("Index must be positive");
            }
            throw new IndexOutOfBoundsException("List is empty");
        }

        default <O> O foldLeft(O accumulator, @NonNull BiFunction<O, E, O> mapper) {
            Objects.requireNonNull(mapper, "mapper");
            return accumulator;
        }

        default <O> O foldRight(O accumulator, @NonNull BiFunction<O, E, O> mapper) {
            Objects.requireNonNull(mapper, "mapper");
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
