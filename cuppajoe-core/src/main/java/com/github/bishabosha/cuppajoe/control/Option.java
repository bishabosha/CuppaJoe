/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.control;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.functions.Func0;
import com.github.bishabosha.cuppajoe.tuples.Unapply0;
import com.github.bishabosha.cuppajoe.tuples.Unapply1;
import com.github.bishabosha.cuppajoe.tuples.Unit;
import com.github.bishabosha.cuppajoe.typeclass.applicative.Applicative1;
import com.github.bishabosha.cuppajoe.typeclass.monad.Monad1;
import com.github.bishabosha.cuppajoe.typeclass.peek.Peek1;
import com.github.bishabosha.cuppajoe.typeclass.value.Value1;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.*;
import java.util.function.*;

public interface Option<E> extends Monad1<Option, E>, Peek1<E>, Value1<Option, E> {

    static <O> Option<O> from(@NonNull Optional<O> optional) {
        Objects.requireNonNull(optional, "optional");
        return optional.map(Option::of).orElse(empty());
    }

    static <O> Option<O> of(O value) {
        return empty().pure(value);
    }

    static <O> Option<O> when(@NonNull BooleanSupplier condition, @NonNull Func0<O> elem) {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(elem, "elem");
        return condition.getAsBoolean() ? some(elem.get()) : empty();
    }

    static <O> Some<O> some(O value) {
        return new Some<>(value);
    }

    static Option.None none() {
        return None.INSTANCE;
    }

    static <O> Option<O> empty() {
        return Monad1.Type.<Option<O>, Option, O>castParam(None.INSTANCE);
    }

    @Override
    default E get() {
        throw new NoSuchElementException("There is nothing present.");
    }

    @Override
    default Option<E> or(@NonNull Supplier<? extends Value1<Option, ? extends E>> alternative) {
        Objects.requireNonNull(alternative, "alternative");
        return isEmpty() ? Value1.Type.<Option<E>, Option, E>narrow(alternative.get()) : this;
    }

    default Option<E> filter(@NonNull Predicate<? super E> filter) {
        Objects.requireNonNull(filter, "filter");
        return !isEmpty() && filter.test(get()) ? this : empty();
    }

    @Override
    default <U> Option<U> map(@NonNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return isEmpty() ? empty() : some(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> flatMap(@NonNull Function<? super E, Monad1<Option, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return isEmpty() ? empty() : Objects.requireNonNull(Monad1.Type.<Option<U>, Option, U>narrow(mapper.apply(get())));
    }

    default <R> Option<R> cast(@NonNull Class<R> clazz) {
        Objects.requireNonNull(clazz, "clazz");
        return !isEmpty() && clazz.isInstance(get()) ? some(clazz.cast(get())) : empty();
    }

    default boolean contains(E e) {
        return !isEmpty() && Objects.equals(get(), e);
    }

    @Override
    default void peek(@NonNull Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer, "consumer");
        if (!isEmpty()) {
            consumer.accept(get());
        }
    }

    @Override
    default Option<E> toOption() {
        return this;
    }

    @Override
    default <U> Option<U> pure(U value) {
        return value == null ? empty() : some(value);
    }

    @Override
    default <U> Option<U> apply(@NonNull Applicative1<Option, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    @Override
    default Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    final class Some<O> implements Option<O>, Unapply1<O> {

        private O value;

        @Override
        public O unapply() {
            return get();
        }

        private Some(O value) {
            this.value = value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public O get() {
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(get());
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof Some && Objects.equals(((Some) obj).get(), get());
        }

        @Override
        public String toString() {
            return "Some(" + get() + ")";
        }

        @Override
        public Iterator<O> iterator() {
            return Iterators.singletonSupplier(this::get);
        }
    }

    enum None implements Option<Unit>, Unapply0 {

        INSTANCE;

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public String toString() {
            return "None";
        }
    }
}

