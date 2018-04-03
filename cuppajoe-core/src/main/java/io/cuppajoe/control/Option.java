/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.control;

import io.cuppajoe.Iterators;
import io.cuppajoe.tuples.Tuple1;
import io.cuppajoe.tuples.Unapply0;
import io.cuppajoe.tuples.Unapply1;
import io.cuppajoe.tuples.Unit;
import io.cuppajoe.typeclass.applicative.Applicative1;
import io.cuppajoe.typeclass.monad.Monad1;
import io.cuppajoe.typeclass.peek.Peek1;
import io.cuppajoe.typeclass.value.Value1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.cuppajoe.API.Tuple;

public interface Option<E> extends Monad1<Option, E>, Peek1<E>, Value1<Option, E> {

    static <O> Option<O> from(Optional<O> optional) {
        return optional.map(Option::of).orElse(empty());
    }

    static <O> Option<O> of(O value) {
        return empty().pure(value);
    }

    @NotNull
    @Contract(pure = true)
    static <O> Some<O> some(@Nullable O value) {
        return new Some<>(value);
    }

    @NotNull
    @Contract(pure = true)
    static Option.None none() {
        return None.INSTANCE;
    }

    @NotNull
    @Contract(pure = true)
    static <O> Option<O> empty() {
        return Monad1.Type.<Option<O>, Option, O>castParam(None.INSTANCE);
    }

    @Contract(" -> fail")
    @Override
    default E get() {
        throw new NoSuchElementException("There is nothing present.");
    }

    @Override
    default Option<E> or(Supplier<? extends Value1<Option, ? extends E>> alternative) {
        return isEmpty() ? Value1.Type.<Option<E>, Option, E>narrow(alternative.get()) : this;
    }

    default Option<E> filter(Predicate<? super E> filter) {
        return !isEmpty() && filter.test(get()) ? this : empty();
    }

    @Override
    default <U> Option<U> map(Function<? super E, ? extends U> mapper) {
        return isEmpty() ? empty() : some(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> flatMap(Function<? super E, Monad1<Option, ? extends U>> mapper) {
        return isEmpty() ? empty() : Objects.requireNonNull(Monad1.Type.<Option<U>, Option, U>narrow(mapper.apply(get())));
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return !isEmpty() && clazz.isInstance(get()) ? some(clazz.cast(get())) : empty();
    }

    default boolean contains(E e) {
        return !isEmpty() && Objects.equals(get(), e);
    }

    @Override
    default void peek(Consumer<? super E> consumer) {
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
    default <U> Option<U> apply(Applicative1<Option, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    @NotNull
    @Override
    default Iterator<E> iterator() {
        return Iterators.empty();
    }

    final class Some<O> implements Option<O>, Unapply1<O> {

        private O value;

        @Override
        public Tuple1<O> unapply() {
            return Tuple(get());
        }

        private Some(O value) {
            this.value = value;
        }

        @Contract(pure = true)
        @Override
        public boolean isEmpty() {
            return false;
        }

        @Contract(pure = true)
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

        @NotNull
        @Contract(pure = true)
        @Override
        public String toString() {
            return "Some(" + get() + ")";
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public Iterator<O> iterator() {
            return Iterators.singletonSupplier(this::get);
        }
    }

    enum None implements Option<Unit>, Unapply0 {

        INSTANCE;

        @Contract(pure = true)
        @Override
        public boolean isEmpty() {
            return true;
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public String toString() {
            return "None";
        }
    }
}

