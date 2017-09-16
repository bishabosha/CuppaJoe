/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.functions.Func0;
import com.bishabosha.caffeine.functional.functions.Func1;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.patterns.PatternResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Option<O> extends Iterable<O> {

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

    boolean isSome();

    O get();

    default O orElse(O alternative) {
        return isSome() ? get() : alternative;
    }

    default O orElseGet(Supplier<O> alternative) {
        return isSome() ? get() : alternative.get();
    }

    default <X extends Throwable> O orElseThrow(Supplier<? extends X> exceptionGet) throws X {
        if (isSome()) {
            return get();
        }
        throw exceptionGet.get();
    }

    default Option<O> join(Supplier<Option<O>> alternative) {
        return isSome() ? this : alternative.get();
    }

    default Option<O> filter(Predicate<O> filter) {
        return isSome() && filter.test(get()) ? this : Nothing.getInstance();
    }

    default <T> Option<T> map(Func1<O, T> mapper) {
        return isSome() ? Some.of(mapper.apply(get())) : Nothing.getInstance();
    }

    default <T> Option<T> flatMap(Func1<O, Option<T>> mapper) {
        final Option<T> result;
        if (isSome() && (result = mapper.apply(get())) instanceof Some) {
            return result;
        }
        return Nothing.getInstance();
    }

    default <T> Option<T> match(Pattern toMatch, Func1<PatternResult, T> mapper) {
        return isSome() ? toMatch.test(get()).map(mapper) : Nothing.getInstance();
    }

    default <T> Option<T> match(Case<O, T> matcher) {
        return isSome() ? matcher.match(get()) : Nothing.getInstance();
    }

    default <T> Option<T> flatMatch(Case<O, Option<T>> toMatch) {
        return isSome() ? toMatch.match(get()).orElse(Nothing.getInstance()) : Nothing.getInstance();
    }

    default Option<?> unwrap() {
        if (isSome() && get() instanceof Option<?>) {
            return (Option<?>) get();
        }
        return Nothing.getInstance();
    }

    default <T> Option<T> flatMapOrElse(Func1<O, Option<T>> mapper, Supplier<Option<T>> orElse) {
        return isSome() ? mapper.apply(get()) : orElse.get();
    }

    default Option<O> ifNothing(Runnable toDo) {
        if (!isSome()) {
            toDo.run();
        }
        return this;
    }

    default Option<O> ifSome(Consumer<O> toDo) {
        if (isSome()) {
            toDo.accept(get());
        }
        return this;
    }

    default Option<O> ifSomeOrElse(Consumer<O> toDo, Runnable alternative) {
        if (isSome()) {
            toDo.accept(get());
        } else {
            alternative.run();
        }
        return this;
    }

    default Option<Option<O>> wrap() {
        return isSome() ? of(this) : of(Nothing.getInstance());
    }

    default Option<Option<O>> wrapifPresent() {
        return isSome() ? of(this) : Nothing.getInstance();
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return isSome() && clazz.isInstance(get()) ? Some.of(clazz.cast(get())) : Nothing.getInstance();
    }

    default boolean contains(Object o) {
        return isSome() && Objects.equals(get(), o);
    }

    default int size() {
        return isSome() ? 1 : 0;
    }

    default Optional<O> toJavaOptional() {
        return isSome() ? Optional.ofNullable(get()) : Optional.empty();
    }
}

