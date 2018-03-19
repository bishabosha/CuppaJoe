/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.typeclass.applicative.Applicative1;
import com.bishabosha.cuppajoe.typeclass.monad.Monad1;
import com.bishabosha.cuppajoe.typeclass.peek.Peek1;
import com.bishabosha.cuppajoe.typeclass.value.Value1;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Option<O> extends Monad1<Option, O>, Peek1<O>, Value1<Option, O> {

    static <O> Option<O> from(Optional<O> optional) {
        return optional.map(Option::of).orElse(Nothing.getInstance());
    }

    static <O> Option<O> of(O value) {
        return Nothing.getInstance().pure(value);
    }

    @Override
    default Option<O> or(Supplier<? extends Value1<Option, ? extends O>> alternative) {
        return isEmpty() ? Value1.Type.<Option<O>, Option, O>narrow(alternative.get()) : this;
    }

    default Option<O> filter(Predicate<? super O> filter) {
        return !isEmpty() && filter.test(get()) ? this : Nothing.getInstance();
    }

    @Override
    default <U> Option<U> map(Function<? super O, ? extends U> mapper) {
        return isEmpty() ? Nothing.getInstance() : Some.of(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> flatMap(Function<? super O, Monad1<Option, ? extends U>> mapper) {
        return isEmpty() ? Nothing.getInstance() : Objects.requireNonNull(Monad1.Type.<Option<U>, Option, U>narrow(mapper.apply(get())));
    }

    default <T> Option<T> match(Case<? super O, T> matcher) {
        return isEmpty() ? Nothing.getInstance() : matcher.match(get());
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return !isEmpty() && clazz.isInstance(get()) ? Some.of(clazz.cast(get())) : Nothing.getInstance();
    }

    default boolean contains(O o) {
        return !isEmpty() && Objects.equals(get(), o);
    }

    @Override
    default void peek(Consumer<? super O> consumer) {
        if (!isEmpty()) {
            consumer.accept(get());
        }
    }

    @Override
    default Option<O> toOption() {
        return this;
    }

    @Override
    default <U> Option<U> pure(U value) {
        return value == null ? Nothing.getInstance() : Some.of(value);
    }

    @Override
    default <U> Option<U> apply(Applicative1<Option, Function<? super O, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }
}

