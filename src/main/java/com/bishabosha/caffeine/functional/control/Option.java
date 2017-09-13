/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.base.AbstractArrayHelper;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.patterns.PatternResult;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;

public final class Option<O> extends AbstractArrayHelper<O> {

    private static final Option<?> EMPTY = new Option<>(null);

    public static Pattern Some(Pattern pattern) {
        return patternFor(Option.class).conditionalAtomic(Option::isSome, Option::get, pattern);
    }

    public static final Pattern Nothing() {
        return x -> EMPTY.equals(x) ? Pattern.PASS : Pattern.FAIL;
    }

    private O value;

    private Option(O value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <O> Option<O> nothing() {
        return (Option<O>) EMPTY;
    }

    public static <O> Option<Option<O>> wrappedNothing() {
        return of(nothing());
    }

    public static <O> Option<O> from(Optional<O> optional) {
        return optional.isPresent() ? of(optional.get()) : nothing();
    }

    public static <O> Option<O> of(O value) {
        return new Option<>(Objects.requireNonNull(value, "Choose Option.ofUnknown() to pass null pointers"));
    }

    public static <O> Option<O> ofUnknown(O value) {
        return Objects.isNull(value) ? nothing() : new Option<>(value);
    }

    public boolean isSome() {
        return value != null;
    }

    public O get() {
        if (value == null) {
            throw new IllegalStateException("There is nothing present.");
        }
        return value;
    }

    public O orElse(O alternative) {
        return value != null ? value : alternative;
    }

    public O orElseGet(Supplier<O> alternative) {
        return value != null ? value : alternative.get();
    }

    public <X extends Throwable> O orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public Option<O> join(Supplier<Option<O>> alternative) {
        return value != null ? this : Objects.requireNonNull(alternative.get());
    }

    public static <R, T extends R, O extends R> Option<R> joinAny(Option<O> option, Supplier<Option<T>> alternative) {
        return option.isSome() ? (Option<R>) option : Objects.requireNonNull((Option<R>) alternative.get());
    }

    public Option<O> filter(Predicate<O> filter) {
        return (value != null && filter.test(value)) ? this : nothing();
    }

    public <T> Option<T> map(Function<O, T> mapper) {
        return value != null ? Option.ofUnknown(mapper.apply(value)) : nothing();
    }

    public <T> Option<T> flatMap(Function<O, Option<T>> mapper) {
        return value != null ? Objects.requireNonNull(mapper.apply(value)) : nothing();
    }

    public <T> Option<T> match(Pattern toMatch, Function<PatternResult, T> mapper) {
        return value != null ? toMatch.test(value).map(mapper) : nothing();
    }

    public <T> Option<T> match(Case<O, T> matcher) {
        return value != null ? matcher.match(value) : nothing();
    }

    public <T> Option<T> flatMatch(Case<O, Option<T>> toMatch) {
        return value != null ? toMatch.match(value).orElse(nothing()) : nothing();
    }

    public Option<?> unwrap() {
        return value instanceof Option ? (Option<?>) value : nothing();
    }

    public O flatten() {
        return orElse(null);
    }

    public <T> Option<T> flatMapOrElse(Function<O, Option<T>> mapper, Supplier<Option<T>> orElse) {
        return value != null ? Objects.requireNonNull(mapper.apply(value)) : Objects.requireNonNull(orElse.get());
    }

    public <T> Option<T> ifNothing(Runnable toDo) {
        if (value == null) {
            toDo.run();
        }
        return (Option<T>) this;
    }

    public Option<O> ifSome(Consumer<O> toDo) {
        if (value != null) {
            toDo.accept(value);
        }
        return this;
    }

    public Option<O> ifSomeOrElse(Consumer<O> toDo, Runnable alternative) {
        if (value != null) {
            toDo.accept(value);
        } else {
            alternative.run();
        }
        return this;
    }

    public Option<Option<O>> wrap() {
        return value == null ? of(nothing()) : of(this);
    }

    public Option<Option<O>> wrapifPresent() {
        return value == null ? nothing() : of(this);
    }

    public <R> Option<R> cast(Class<R> clazz) {
        return value != null && clazz.isInstance(value) ? of(clazz.cast(value)) : nothing();
    }

    @Override
    public String toString() {
        return value == null ? "Nothing" : String.format("Some(%s)", value);
    }

    @Override
    public boolean contains(Object o) {
        return Objects.equals(value, o);
    }

    @Override
    public int size() {
        return value == null ? 0 : 1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ? true : Option.ofUnknown(obj)
                                          .cast(Option.class)
                                          .map(o -> Objects.equals(o.value, value))
                                          .orElse(false);
    }

    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>() {
            boolean unwrapped = false;

            @Override
            public boolean hasNext() {
                return !unwrapped && value != null;
            }

            @Override
            public O next() {
                unwrapped = true;
                return value;
            }
        };
    }

    public Optional<O> asOptional() {
        return Optional.ofNullable(value);
    }
}

