package io.cuppajoe.control;

import io.cuppajoe.typeclass.applicative.Applicative1;
import io.cuppajoe.typeclass.functor.Functor1;
import io.cuppajoe.typeclass.functor.Functor2;
import io.cuppajoe.typeclass.monad.Monad1;
import io.cuppajoe.typeclass.peek.Peek1;
import io.cuppajoe.typeclass.peek.Peek2;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Either<L, R> extends Functor2<Either, L, R>, Peek2<L, R> {

    boolean isLeft();

    L left();

    R right();

    default <U1, U2> Either<U1, U2> map(Function<? super L, ? extends U1> ifLeft, Function<? super R, ? extends U2> ifRight) {
        Objects.requireNonNull(ifLeft);
        Objects.requireNonNull(ifRight);
        return isLeft() ? Either.left(ifLeft.apply(left())) : Either.right(ifRight.apply(right()));
    }

    default <O> O transform(Function<? super L, ? extends O> ifLeft, Function<? super R, ? extends O> ifRight) {
        Objects.requireNonNull(ifLeft);
        Objects.requireNonNull(ifRight);
        return isLeft() ? ifLeft.apply(left()) : ifRight.apply(right());
    }

    default void peek(Consumer<? super L> ifLeft, Consumer<? super R> ifRight) {
        Objects.requireNonNull(ifRight);
        Objects.requireNonNull(ifLeft);
        if (isLeft()) {
            ifLeft.accept(left());
        } else {
            ifRight.accept(right());
        }
    }

    default LeftProjection<L, R> leftProject() {
        return new LeftProjection<>(this);
    }

    default RightProjection<L, R> rightProject() {
        return new RightProjection<>(this);
    }

    static <L, R> Left<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Right<L, R> right(R right) {
        return new Right<>(right);
    }

    interface Projection<L, R> {
        Either<L, R> restore();
    }

    class LeftProjection<L, R> implements Projection<L, R>, Monad1<LeftProjection, L>, Peek1<L> {

        private final Either<L, R> value;

        @Override
        public Either<L, R> restore() {
            return value;
        }

        private LeftProjection(Either<L, R> value) {
            this.value = value;
        }

        @Override
        public <U> LeftProjection<U, R> apply(Applicative1<LeftProjection, Function<? super L, ? extends U>> applicative1) {
            Objects.requireNonNull(applicative1);
            LeftProjection<Function<? super L, ? extends U>, R> app = Applicative1.Type.narrow(applicative1);
            return app.restore().isLeft()
                    ? map(app.restore().left())
                    : Monad1.Type.<LeftProjection<U, R>, LeftProjection, U>castParam(this);
        }

        @Override
        public <U> LeftProjection<U, R> pure(U value) {
            return new Left<U, R>(value).leftProject();
        }

        @Override
        public <U> LeftProjection<U, R> map(Function<? super L, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return restore().isLeft() ? new LeftProjection<>(new Left<>(mapper.apply(restore().left()))) : Functor1.Type.<LeftProjection<U, R>, LeftProjection, U>castParam(this);
        }

        @Override
        public <U> LeftProjection<U, R> flatMap(Function<? super L, Monad1<LeftProjection, ? extends U>> mapper) {
            Objects.requireNonNull(mapper);
            return restore().isLeft()
                    ? Monad1.Type.<LeftProjection<U, R>, LeftProjection, U>narrow(mapper.apply(restore().left()))
                    : Monad1.Type.<LeftProjection<U, R>, LeftProjection, U>castParam(this);
        }

        @Override
        public void peek(Consumer<? super L> consumer) {
            Objects.requireNonNull(consumer);
            if (restore().isLeft()) {
                consumer.accept(restore().left());
            }
        }

        public <X extends Exception> void throwLeft(Function<? super L, ? extends X> exceptionMapper) throws X {
            Objects.requireNonNull(exceptionMapper);
            if (restore().isLeft()) {
                throw exceptionMapper.apply(restore().left());
            }
        }
    }

    class RightProjection<L, R> implements Projection<L, R>, Monad1<RightProjection, R>, Peek1<R> {

        private final Either<L, R> value;

        private RightProjection(Either<L, R> value) {
            this.value = value;
        }

        @Override
        public Either<L, R> restore() {
            return value;
        }

        @Override
        public <U> RightProjection<L, U> pure(U value) {
            return new Right<L, U>(value).rightProject();
        }

        @Override
        public <U> RightProjection<L, U> apply(Applicative1<RightProjection, Function<? super R, ? extends U>> applicative1) {
            Objects.requireNonNull(applicative1);
            RightProjection<L, Function<? super R, ? extends U>> app = Applicative1.Type.narrow(applicative1);
            return app.restore().isLeft()
                    ? Monad1.Type.<RightProjection<L, U>, RightProjection, U>castParam(this)
                    : map(app.restore().right());
        }

        @Override
        public <U> RightProjection<L, U> flatMap(Function<? super R, Monad1<RightProjection, ? extends U>> mapper) {
            Objects.requireNonNull(mapper);
            return restore().isLeft()
                    ? Monad1.Type.<RightProjection<L, U>, RightProjection, U>castParam(this)
                    : Monad1.Type.<RightProjection<L, U>, RightProjection, U>narrow(mapper.apply(restore().right()));
        }

        @Override
        public <U> RightProjection<L, U> map(Function<? super R, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return !value.isLeft() ? new RightProjection<>(new Right<>(mapper.apply(restore().right()))) : Functor1.Type.<RightProjection<L, U>, RightProjection, U>castParam(this);
        }

        @Override
        public void peek(Consumer<? super R> consumer) {
            Objects.requireNonNull(consumer);
            if (!value.isLeft()) {
                consumer.accept(restore().right());
            }
        }
    }

    class Left<L, R> implements Either<L, R> {

        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public L left() {
            return value;
        }

        @Override
        public R right() throws NoSuchElementException {
            throw new NoSuchElementException("No Right element on Left kind.");
        }

        @Override
        public String toString() {
            return "Left(" + value + ")";
        }
    }

    class Right<L, R> implements Either<L, R> {

        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public L left() throws NoSuchElementException {
            throw new NoSuchElementException("No Left element on Right kind.");
        }

        @Override
        public R right() {
            return value;
        }

        @Override
        public String toString() {
            return "Right(" + value + ")";
        }
    }
}
