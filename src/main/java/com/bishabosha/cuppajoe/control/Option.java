/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Value;
import com.bishabosha.cuppajoe.functions.*;
import com.bishabosha.cuppajoe.patterns.Case;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternResult;
import com.bishabosha.cuppajoe.typeclass.Applicative;
import com.bishabosha.cuppajoe.typeclass.Monad;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Option<O> extends Value<O>, Monad<Option, O> {

    static <O> Option<O> from(Optional<O> optional) {
        return optional.map(Option::of).orElseGet(Nothing::getInstance);
    }

    @NotNull
    @Contract(pure = true)
    static <O> Option<O> of(O value) {
        return Nothing.<O>getInstance().pure(value);
    }

    @Override
    default int size() {
        return isEmpty() ? 0 : 1;
    }

    @SuppressWarnings("unchecked")
    default Option<O> or(Supplier<? extends Option<? extends O>> supplier) {
        return isEmpty() ? (Option<O>) supplier.get() : this;
    }

    default Option<O> filter(Predicate<? super O> filter) {
        return !isEmpty() && filter.test(get()) ? this : Nothing.getInstance();
    }

    @Override
    default <U> Option<U> map(Function<? super O, ? extends U> mapper) {
        return isEmpty() ? Nothing.getInstance() : Some.of(mapper.apply(get()));
    }

    @Override
    default <U> Option<U> flatMap(Function<? super O, Monad<Option, ? extends U>> mapper) {
        return isEmpty() ? Nothing.getInstance() : Objects.requireNonNull((Option<U>) mapper.apply(get()));
    }

    default <T> Option<T> match(Pattern toMatch, Func1<PatternResult, T> mapper) {
        return isEmpty() ? Nothing.getInstance() : toMatch.test(get()).map(mapper);
    }

    default <T> Option<T> match(Case<? super O, T> matcher) {
        return isEmpty() ? Nothing.getInstance() : matcher.match(get());
    }

    default <R> Option<R> cast(Class<R> clazz) {
        return !isEmpty() && clazz.isInstance(get()) ? Some.of(clazz.cast(get())) : Nothing.getInstance();
    }

    @Override
    default Option<O> pure(O value) {
        return value == null ? Nothing.getInstance() : Some.of(value);
    }

    @Override
    default <U> Option<U> apply(Applicative<Option, Function<? super O, ? extends U>> applicative) {
        final Option<Function<? super O, ? extends U>> functionOption = (Option<Function<? super O, ? extends U>>) applicative;
        return functionOption.flatMap(this::map);
    }

    @Override
    default <T, S> Func1<Option<T>, Option<S>> lift(Function<T, S> function) {
        return o -> o.map(function);
    }

    @Override
    default <T, U, S> Func2<Option<T>, Option<U>, Option<S>> lift2(BiFunction<T, U, S> function) {
        return (o1, o2) -> o1.flatMap(
            a -> o2.map(
                b -> function.apply(a, b)));
    }

    @Override
    default <T, U, V, S> Func3<Option<T>, Option<U>, Option<V>, Option<S>> lift3(Func3<T, U, V, S> function) {
        return (o1, o2, o3) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.map(
                    c -> function.apply(a, b, c))));
    }

    @Override
    default <T, U, V, M, S> Func4<Option<T>, Option<U>, Option<V>, Option<M>, Option<S>> lift4(Func4<T, U, V, M, S> function) {
        return (o1, o2, o3, o4) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.flatMap(
                    c -> o4.map(
                        d -> function.apply(a, b, c, d)))));
    }

    @Override
    default <T, U, V, M, R, S> Func5<Option<T>, Option<U>, Option<V>, Option<M>, Option<R>, Option<S>> lift5(Func5<T, U, V, M, R, S> function) {
        return (o1, o2, o3, o4, o5) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.flatMap(
                    c -> o4.flatMap(
                        d -> o5.map(
                            e -> function.apply(a, b, c, d, e))))));
    }

    @Override
    default <T, U, V, M, R, D, S> Func6<Option<T>, Option<U>, Option<V>, Option<M>, Option<R>, Option<D>, Option<S>> lift6(Func6<T, U, V, M, R, D, S> function) {
        return (o1, o2, o3, o4, o5, o6) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.flatMap(
                    c -> o4.flatMap(
                        d -> o5.flatMap(
                            e -> o6.map(
                                f -> function.apply(a, b, c, d, e, f)))))));

    }

    @Override
    default <T, U, V, M, R, D, Q, S> Func7<Option<T>, Option<U>, Option<V>, Option<M>, Option<R>, Option<D>, Option<Q>, Option<S>> lift7(Func7<T, U, V, M, R, D, Q, S> function) {
        return (o1, o2, o3, o4, o5, o6, o7) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.flatMap(
                    c -> o4.flatMap(
                        d -> o5.flatMap(
                            e -> o6.flatMap(
                                f -> o7.map(g -> function.apply(a, b, c, d, e, f, g))))))));
    }

    @Override
    default <T, U, V, M, R, D, Q, F, S> Func8<Option<T>, Option<U>, Option<V>, Option<M>, Option<R>, Option<D>, Option<Q>, Option<F>, Option<S>> lift8(Func8<T, U, V, M, R, D, Q, F, S> function) {
        return (o1, o2, o3, o4, o5, o6, o7, o8) -> o1.flatMap(
            a -> o2.flatMap(
                b -> o3.flatMap(
                    c -> o4.flatMap(
                        d -> o5.flatMap(
                            e -> o6.flatMap(
                                f -> o7.flatMap(
                                    g -> o8.map(
                                        h -> function.apply(a, b, c, d, e, f, g, h)))))))));
    }

    default boolean contains(O o) {
        return !isEmpty() && Objects.equals(get(), o);
    }

    @Override
    default Option<O> toOption() {
        return this;
    }
}

