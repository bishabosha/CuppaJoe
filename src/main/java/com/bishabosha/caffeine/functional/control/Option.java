/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.Value;
import com.bishabosha.caffeine.functional.functions.Func1;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.patterns.PatternResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Option<O> extends Value<O> {

    static <O> Option<Option<O>> wrappedNothing() {
        return of(Nothing.getInstance());
    }

    static <O> Option<O> from(Optional<O> optional) {
        return optional.isPresent() ? of(optional.get()) : Nothing.getInstance();
    }

    @NotNull
    @Contract(pure = true)
    static <O> Option<O> of(O value) {
        return Objects.nonNull(value) ? Some.of(value) : Nothing.getInstance();
    }

    @SuppressWarnings("unchecked")
    default Option<O> join(Supplier<? extends Option<? extends O>> supplier) {
        return isEmpty() ? (Option<O>) supplier.get() : this;
    }

    default Option<O> filter(Predicate<? super O> filter) {
        return !isEmpty() && filter.test(get()) ? this : Nothing.getInstance();
    }

    @Override
    default <R> Option<R> map(Function<? super O, ? extends R> mapper) {
        return isEmpty() ? Nothing.getInstance() : Some.of(mapper.apply(get()));
    }

    @SuppressWarnings("unchecked")
    default <T> Option<T> flatMap(Func1<? super O, ? extends Option<? extends T>> mapper) {
        final Option<? extends T> result;
        if (!isEmpty() && (result = mapper.apply(get())) instanceof Some) {
            return (Option<T>) result;
        }
        return Nothing.getInstance();
    }

    default <T> Option<T> match(Pattern toMatch, Func1<PatternResult, T> mapper) {
        return isEmpty() ? Nothing.getInstance() : toMatch.test(get()).map(mapper);
    }

    default <T> Option<T> match(Case<O, T> matcher) {
        return isEmpty() ? Nothing.getInstance() : matcher.match(get());
    }

    default <T> Option<T> flatMatch(Case<O, Option<T>> toMatch) {
        return isEmpty() ? Nothing.getInstance() : toMatch.match(get()).orElse(Nothing.getInstance());
    }

    default Option<?> unwrap() {
        if (!isEmpty() && get() instanceof Option<?>) {
            return (Option<?>) get();
        }
        return Nothing.getInstance();
    }

    default <T> Option<T> flatMapOrElse(Func1<O, Option<T>> mapper, Supplier<Option<T>> orElse) {
        return isEmpty() ? orElse.get() : mapper.apply(get());
    }

    default Option<Option<O>> wrap() {
        return isEmpty() ? of(Nothing.getInstance()) : of(this);
    }

    default Option<Option<O>> wrapifPresent() {
        return isEmpty() ? Nothing.getInstance() : of(this);
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return !isEmpty() && clazz.isInstance(get()) ? Some.of(clazz.cast(get())) : Nothing.getInstance();
    }

    default boolean contains(O o) {
        return !isEmpty() && Objects.equals(get(), o);
    }

    @Override
    default boolean isAtMaxSingleElement() {
        return true;
    }

    @Override
    default Option<O> toOption() {
        return this;
    }
}

