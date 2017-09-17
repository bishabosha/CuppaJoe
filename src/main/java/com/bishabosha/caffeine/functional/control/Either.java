/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.control;

import com.bishabosha.caffeine.functional.Value;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.patterns.Case.when;

public interface Either<L, R> extends Value<R> {

    default Option<L> maybeLeft() {
        return when(this::isLeft, this::getLeft).match();
    }

    default Option<R> maybeRight() {
        return when(this::isRight, this::get).match();
    }

    default L getLeftOrElse(Supplier<? extends L> supplier) {
        return isRight() ? supplier.get() : getLeft();
    }

    default R getRightOrElse(Supplier<? extends R> supplier) {
        return orElseGet(supplier);
    }

    L getLeft();

    boolean isRight();

    boolean isLeft();

    @Override
    default boolean isAtMaxSingleElement() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    default <X> Either<L, X> map(Function<? super R, ? extends X> mapper) {
        return isRight() ? Right.of(mapper.apply(get())) : (Either<L, X>) this;
    }

    default <A, B> Either<A, B> biMap(Function<? super L, ? extends A> ifLeft, Function<? super R, ? extends B> ifRight) {
        return isRight() ? Right.of(ifRight.apply(get())) : Left.of(ifLeft.apply(getLeft()));
    }

    @SuppressWarnings("unchecked")
    default <A, B> Either<A, B> flatBiMap(Function<? super L, ? extends Either<? extends A, ? extends B>> ifLeft, Function<? super R, ? extends Either<? extends A, ? extends B>> ifRight) {
        return (Either<A, B>) (isRight()
            ? Objects.requireNonNull(ifRight.apply(get()))
            : Objects.requireNonNull(ifLeft.apply(getLeft())));
    }
}
