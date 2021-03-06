package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Unit;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.higher.applicative.Applicative1;
import com.github.bishabosha.cuppajoe.higher.foldable.Foldable;
import com.github.bishabosha.cuppajoe.higher.monad.Monad1;
import com.github.bishabosha.cuppajoe.higher.monoid.Monoid1;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Validation<E, O> extends Monad1<Validation<E, ?>, O>, Monoid1<Validation<?, O>, E> {

    static <E, O> Validation<E, O> of(O value) {
        return new Accumulate<>(value, List.empty());
    }

    static <E, O> Validation<E, O> accumulate(O value, @NonNull Predicate<O> test, @NonNull Function<O, E> ifFail) {
        return new Accumulate<E, O>(value, List.empty()).accumulate(test, ifFail);
    }

    static <E, O> Validation<E, O> shortcut(O value, @NonNull Predicate<O> test, @NonNull Function<O, E> ifFail) {
        return new Accumulate<E, O>(value, List.empty()).shortcut(test, ifFail);
    }

    O value();

    List<E> errorStack();

    boolean isSink();

    default Either<List<E>, Unit> evaluate() {
        return errorStack().isEmpty() ? Either.right(Unit.INSTANCE) : Either.left(errorStack().reverse());
    }

    default Validation<E, O> accumulate(@NonNull Predicate<O> test, @NonNull Function<O, E> ifFail) {
        Objects.requireNonNull(test, "test");
        Objects.requireNonNull(ifFail, "ifFail");
        return isSink() || test.test(value()) ? this : new Accumulate<>(value(), errorStack().push(ifFail.apply(value())));
    }

    default Validation<E, O> shortcut(@NonNull Predicate<O> test, @NonNull Function<O, E> ifFail) {
        Objects.requireNonNull(test, "test");
        Objects.requireNonNull(ifFail, "ifFail");
        return isSink() || test.test(value()) ? this : new Sink<>(errorStack().push(ifFail.apply(value())));
    }

    default Validation<E, O> filter(@NonNull Predicate<O> condition) {
        Objects.requireNonNull(condition, "condition");
        return isSink() || condition.test(value()) ? this : new Sink<>(errorStack());
    }

    @Override
    default <U> Validation<E, U> map(@NonNull Function<? super O, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return isSink()
                ? Monad1.Type.<Validation<E, U>, Validation<E, ?>, U>castParam(this)
                : new Accumulate<>(mapper.apply(value()), errorStack());
    }

    @Override
    default <U> Validation<E, U> flatMap(@NonNull Function<? super O, Monad1<Validation<E, ?>, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        if (isSink()) {
            return Monad1.Type.<Validation<E, U>, Validation<E, ?>, U>castParam(this);
        }
        var next = Monad1.Type.<Validation<E, U>, Validation<E, ?>, U>narrow(mapper.apply(value()));
        var accumulated = next.errorStack().mappend(errorStack());
        return next.isSink()
                ? new Sink<>(accumulated)
                : new Accumulate<>(next.value(), accumulated);
    }

    @Override
    default <U> Validation<E, U> pure(U value) {
        return new Accumulate<>(value, errorStack());
    }

    @Override
    default Validation<E, O> mempty() {
        return of(value());
    }

    @Override
    default Validation<E, O> mconcat(@NonNull Foldable<Monoid1<Validation<?, O>, ? extends E>> list) {
        return Monoid1.Type.narrow(Monoid1.super.mconcat(list));
    }

    @Override
    default Validation<E, O> mappend(@NonNull Monoid1<Validation<?, O>, ? extends E> other) {
        Objects.requireNonNull(other, "other");
        return flatMap(o -> Monoid1.Type.narrow(other));
    }

    @Override
    default <U> Validation<E, U> apply(@NonNull Applicative1<Validation<E, ?>, Function<? super O, ? extends U>> applicative) {
        Objects.requireNonNull(applicative, "applicative");
        return Monad1.Type.<Validation<E, U>, Validation<E, ?>, U>narrow(
                Monad1.Type.lA(applicative).flatMap(f -> map(x -> f.apply(x))));
    }

    class Accumulate<E, O> implements Validation<E, O> {

        private final List<E> errorStack;
        private final O value;

        @Override
        public boolean isSink() {
            return false;
        }

        @Override
        public List<E> errorStack() {
            return errorStack;
        }

        @Override
        public O value() {
            return value;
        }

        private Accumulate(O value, List<E> errors) {
            this.value = value;
            this.errorStack = errors;
        }
    }

    class Sink<E, O> implements Validation<E, O> {

        private final List<E> errorStack;

        @Override
        public boolean isSink() {
            return true;
        }

        @Override
        public List<E> errorStack() {
            return errorStack;
        }

        @Override
        public O value() {
            throw new NoSuchElementException("Sink has no value to test");
        }

        private Sink(List<E> errorStack) {
            this.errorStack = errorStack;
        }
    }
}
