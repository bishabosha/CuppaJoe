package com.github.bishabosha.cuppajoe.control;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.applicative.Applicative1;
import com.github.bishabosha.cuppajoe.higher.functions.CheckedFunc0;
import com.github.bishabosha.cuppajoe.higher.monad.Monad1;
import com.github.bishabosha.cuppajoe.higher.peek.Peek1;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply1;
import com.github.bishabosha.cuppajoe.higher.value.Value1;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;

public interface Try<E> extends Monad1<Try, E>, Peek1<E>, Value1<Try, E> {

    boolean isSuccess();

    boolean isFailure();

    Exception getError();

    static <O> Try<O> of(@NonNull CheckedFunc0<O> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        try {
            var value = supplier.apply();
            return new Success<>(value);
        } catch (Exception error) {
            return new Failure<>(error);
        }
    }

    static <O> Success<O> success(O value) {
        return new Success<>(value);
    }

    static <O> Failure<O> failure(@NonNull Exception error) {
        Objects.requireNonNull(error, "error");
        return new Failure<>(error);
    }

    @Override
    default boolean isEmpty() {
        return isFailure();
    }

    @Override
    default <U> Try<U> pure(U value) {
        return new Success<>(value);
    }

    default <O> Try<O> map(@NonNull Function<? super E, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return isSuccess() ? new Success<>(mapper.apply(get())) : Failure.cast(this);
    }

    @Override
    default <U> Try<U> flatMap(@NonNull Function<? super E, Monad1<Try, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return isSuccess() ? Monad1.Type.<Try<U>, Try, U>narrow(mapper.apply(get())) : Failure.cast(this);
    }

    @Override
    default Try<E> or(@NonNull Supplier<? extends Value1<Try, ? extends E>> alternative) {
        Objects.requireNonNull(alternative, "alternative");
        return isSuccess() ? this : Value1.Type.<Try<E>, Try, E>narrow(alternative.get());
    }

    default <X extends Throwable> E orElseThrowMapped(@NonNull Function<Exception, ? extends X> errorMapper) throws X {
        Objects.requireNonNull(errorMapper, "errorMapper");
        if (isSuccess()) {
            return get();
        }
        throw errorMapper.apply(getError());
    }

    default Option<E> lift() {
        if (isSuccess()) {
            return Option.of(get());
        } else {
            return None();
        }
    }


    default Optional<E> liftWhenError(@NonNull Consumer<Exception> ifError) {
        Objects.requireNonNull(ifError, "ifError");
        if (isSuccess()) {
            return Optional.ofNullable(get());
        } else {
            ifError.accept(getError());
            return Optional.empty();
        }
    }

    @Override
    default void peek(@NonNull Consumer<? super E> ifSuccess) {
        Objects.requireNonNull(ifSuccess, "ifSuccess");
        if (isSuccess()) {
            ifSuccess.accept(get());
        }
    }

    default void consumeElse(@NonNull Consumer<E> ifSuccess, @NonNull Consumer<Exception> ifError) {
        Objects.requireNonNull(ifSuccess, "ifSuccess");
        Objects.requireNonNull(ifError, "ifError");
        if (isSuccess()) {
            ifSuccess.accept(get());
        } else {
            ifError.accept(getError());
        }
    }

    default void ifError(@NonNull Consumer<Exception> ifError) {
        Objects.requireNonNull(ifError, "ifError");
        if (isFailure()) {
            ifError.accept(getError());
        }
    }

    @Override
    default <U> Try<U> apply(@NonNull Applicative1<Try, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    @Override
    default Iterator<E> iterator() {
        return isEmpty() ? Collections.emptyIterator() : Iterators.singletonSupplier(this::get);
    }

    final class Success<E> implements Try<E>, Unapply1<E> {

        private final E value;

        private Success(E value) {
            this.value = value;
        }

        @Override
        public final boolean isSuccess() {
            return true;
        }

        @Override
        public final boolean isFailure() {
            return false;
        }

        @Override
        public final E get() {
            return value;
        }

        @Override
        public final Exception getError() {
            throw new NoSuchElementException();
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(get());
        }

        @Override
        public final boolean equals(Object obj) {
            return obj == this || obj instanceof Success && Objects.equals(get(), ((Success) obj).get());
        }

        @Override
        public final String toString() {
            return "Success(" + get() + ")";
        }

        @Override
        public final E unapply() {
            return get();
        }
    }

    final class Failure<E> implements Try<E>, Unapply1<Exception> {

        private final Exception error;

        private Failure(Exception error) {
            this.error = error;
        }

        @Override
        public final boolean isSuccess() {
            return false;
        }

        @Override
        public final boolean isFailure() {
            return true;
        }

        @Override
        public final E get() {
            throw new RuntimeException("Error triggered by operation: " + error.getMessage(), error);
        }

        @Override
        public final Exception getError() {
            return error;
        }

        private static <O, U> Try<U> cast(Try<O> toCast) {
            return Monad1.Type.<Try<U>, Try, U>castParam(toCast);
        }

        @Override
        public final String toString() {
            return "Failure(" + getError().getMessage() + ")";
        }

        @Override
        public final Exception unapply() {
            return getError();
        }
    }
}
