/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.control;

import io.cuppajoe.Iterables;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.patterns.Case;
import io.cuppajoe.patterns.Pattern;
import io.cuppajoe.patterns.PatternFactory;
import io.cuppajoe.tuples.Product1;
import io.cuppajoe.tuples.Unapply0;
import io.cuppajoe.tuples.Unapply1;
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
        return optional.map(Option::of).orElse(Nothing.getInstance());
    }

    static <O> Option<O> of(O value) {
        return Nothing.getInstance().pure(value);
    }

    @NotNull
    @Contract(pure = true)
    static <O> Some<O> some(@Nullable O value) {
        return new Some<>(value);
    }

    @NotNull
    @Contract(pure = true)
    static <O> Nothing<O> nothing() {
        return Nothing.getInstance();
    }

    @Override
    default Option<E> or(Supplier<? extends Value1<Option, ? extends E>> alternative) {
        return isEmpty() ? Value1.Type.<Option<E>, Option, E>narrow(alternative.get()) : this;
    }

    default Option<E> filter(Predicate<? super E> filter) {
        return !isEmpty() && filter.test(get()) ? this : Nothing.getInstance();
    }

    @Override
    default <U> Option<U> map(Function<? super E, ? extends U> mapper) {
        return isEmpty() ? Nothing.getInstance() : some(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> flatMap(Function<? super E, Monad1<Option, ? extends U>> mapper) {
        return isEmpty() ? Nothing.getInstance() : Objects.requireNonNull(Monad1.Type.<Option<U>, Option, U>narrow(mapper.apply(get())));
    }

    default <T> Option<T> match(Case<? super E, T> matcher) {
        return isEmpty() ? Nothing.getInstance() : matcher.match(get());
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return !isEmpty() && clazz.isInstance(get()) ? some(clazz.cast(get())) : Nothing.getInstance();
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
        return value == null ? Nothing.getInstance() : some(value);
    }

    @Override
    default <U> Option<U> apply(Applicative1<Option, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    final class Some<O> implements Option<O>, Unapply1<O> {

        private O value;

        @Override
        public Product1<O> unapply() {
            return Tuple(get());
        }

        private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Some.class);

        @NotNull
        public static Pattern $Some(Pattern pattern) {
            return PATTERN.apply(pattern);
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
            return Iterables.singleton(this::get);
        }
    }

    final class Nothing<O> implements Option<O>, Unapply0 {

        private static final Nothing<Object> NOTHING = new Nothing<>();

        @NotNull
        @Contract(pure = true)
        public static final Pattern $Nothing() {
            return x -> NOTHING.equals(x) ? Pattern.PASS : Pattern.FAIL;
        }

        @NotNull
        @Contract(pure = true)
        private static <O> Nothing<O> getInstance() {
            return Monad1.Type.<Nothing<O>, Option, O>castParam(NOTHING);
        }

        private Nothing() {
        }

        @Contract(pure = true)
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Contract(" -> fail")
        @Override
        public O get() {
            throw new NoSuchElementException("There is nothing present.");
        }

        @NotNull
        @Contract(pure = true)
        @Override
        public String toString() {
            return "Nothing";
        }

        @NotNull
        @Override
        public Iterator<O> iterator() {
            return Iterables.empty();
        }
    }
}

