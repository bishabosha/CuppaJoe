/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.Case.when;

public abstract class Either<L, R> {

    public static final Pattern Left(Pattern pattern) {
        return x -> x instanceof Left ? pattern.test(((Left) x).value) : Pattern.FAIL;
    }

    public static final Pattern Right(Pattern pattern) {
        return x -> x instanceof Right ? pattern.test(((Right) x).value) : Pattern.FAIL;
    }

    public Option<L> getLeft() {
        return when(() -> !isRight(), () -> ((Left<L, R>) this).value).match();
    }

    public Option<R> getRight() {
        return when(() -> isRight(), () -> ((Right<L, R>) this).value).match();
    }

    public L getLeftOrElse(Supplier<L> supplier) {
        return isRight() ? supplier.get() : ((Left<L, R>) this).value;
    }

    public R getRightOrElse(Supplier<R> supplier) {
        return isRight() ? ((Right<L, R>) this).value : supplier.get();
    }

    public abstract boolean isRight();

    public <A, B> Either<A, B> map(Function<L, A> ifLeft, Function<R, B> ifRight) {
        return isRight() ? Right.of(ifRight.apply(((Right<L, R>)this).value)) : Left.of(ifLeft.apply(((Left<L, R>)this).value));
    }

    @SuppressWarnings("unchecked")
    public <A, B> Either<A, B> flatMap(Function<L, Left<A, ?>> ifLeft, Function<R, Right<?, B>> ifRight) {
        return (Either<A, B>) (isRight() ?
            Objects.requireNonNull(ifRight.apply(((Right<L, R>)this).value)) :
            Objects.requireNonNull(ifLeft.apply(((Left<L, R>)this).value)));
    }

    public static class Left<L, R> extends Either<L, R> {
        L value;

        private Left(L value) {
            this.value = value;
        }

        public static <L, R> Left<L, R> of(L left) {
            return new Left<>(Objects.requireNonNull(left, "Either Types are Non-Null"));
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Left)) {
                return false;
            }
            return Objects.equals(value, ((Left) obj).value);
        }

        @Override
        public String toString() {
            return "Left("+value+")";
        }
    }

    public static class Right<L, R> extends Either<L, R> {
        R value;

        private Right(R value) {
            this.value = value;
        }

        public static <L, R> Either<L, R> of(R right) {
            return new Right<>(Objects.requireNonNull(right, "Either Types are Non-Null"));
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Right)) {
                return false;
            }
            return Objects.equals(value, ((Right) obj).value);
        }

        @Override
        public String toString() {
            return "Right("+value+")";
        }
    }
}
