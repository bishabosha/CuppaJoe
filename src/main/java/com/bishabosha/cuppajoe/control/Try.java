package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.functions.CheckedFunc0;
import com.bishabosha.cuppajoe.functions.Func1;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import com.bishabosha.cuppajoe.tuples.Product1;
import com.bishabosha.cuppajoe.tuples.Unapply1;
import com.bishabosha.cuppajoe.typeclass.applicative.Applicative1;
import com.bishabosha.cuppajoe.typeclass.monad.Monad1;
import com.bishabosha.cuppajoe.typeclass.peek.Peek1;
import com.bishabosha.cuppajoe.typeclass.value.Value1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bishabosha.cuppajoe.API.Tuple;

public interface Try<E> extends Monad1<Try, E>, Peek1<E>, Value1<Try, E> {

    boolean isSuccess();
    boolean isFailure();
    Throwable getError();

    @Override
    default boolean isEmpty() {
        return isFailure();
    }

    @Override
    default <U> Try<U> pure(U value) {
        return new Success<>(value);
    }

    default <O> Try<O> map(Function<? super E, ? extends O> mapper) {
        Objects.requireNonNull(mapper);
        return isSuccess() ? new Success<>(mapper.apply(get())) : Failure.cast(this);
    }

    @Override
    default <U> Try<U> flatMap(Function<? super E, Monad1<Try, ? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return isSuccess() ? Monad1.Type.<Try<U>, Try, U>narrow(mapper.apply(get())) : Failure.cast(this);
    }

    interface CheckedConsumer<O> {
        void accept(O o) throws Exception;
    }

    interface CheckedFunction<I, O> {
        O apply(I i) throws Exception;
    }

    static <O> Function<O, Try<Void>> sink(CheckedConsumer<O> consumer) {
        Objects.requireNonNull(consumer);
        return o -> {
            try {
                consumer.accept(o);
                return new Success<>(null);
            } catch (Exception error) {
                return new Failure<>(error);
            }
        };
    }

    static <I, O> Function<I, Try<O>> lift(CheckedFunction<I, O> mapper) {
        Objects.requireNonNull(mapper);
        return i -> {
            try {
                O value = mapper.apply(i);
                return new Success<>(value);
            } catch (Exception error) {
                return new Failure<>(error);
            }
        };
    }

    static <O> Try<O> of(CheckedFunc0<O> supplier) {
        Objects.requireNonNull(supplier);
        try {
            O value = supplier.apply();
            return new Success<>(value);
        } catch (Throwable error) {
            return new Failure<>(error);
        }
    }

    @Override
    default Try<E> or(Supplier<? extends Value1<Try, ? extends E>> alternative) {
        Objects.requireNonNull(alternative);
        return isSuccess() ? this : Value1.Type.<Try<E>, Try, E>narrow(alternative.get());
    }

    default <X extends Throwable> E orElseThrowMapped(Function<Throwable, ? extends X> errorMapper) throws X {
        Objects.requireNonNull(errorMapper);
        if (isSuccess()) {
            return get();
        }
        throw errorMapper.apply(getError());
    }

    default Option<E> lift() {
        if (isSuccess()) {
            return Option.of(get());
        } else {
            return Nothing.getInstance();
        }
    }


    default Optional<E> liftWhenError(Consumer<Throwable> ifError) {
        Objects.requireNonNull(ifError);
        if (isSuccess()) {
            return Optional.ofNullable(get());
        } else {
            ifError.accept(getError());
            return Optional.empty();
        }
    }

    @Override
    default void peek(Consumer<? super E> ifSuccess) {
        Objects.requireNonNull(ifSuccess);
        if (isSuccess()) {
            ifSuccess.accept(get());
        }
    }

    default void consumeElse(Consumer<E> ifSuccess, Consumer<Throwable> ifError) {
        Objects.requireNonNull(ifSuccess);
        Objects.requireNonNull(ifError);
        if (isSuccess()) {
            ifSuccess.accept(get());
        } else {
            ifError.accept(getError());
        }
    }

    default void ifError(Consumer<Throwable> ifError) {
        Objects.requireNonNull(ifError);
        if (isFailure()) {
            ifError.accept(getError());
        }
    }

    @Override
    default <U> Try<U> apply(Applicative1<Try, Function<? super E, ? extends U>> applicative1) {
        return Monad1.applyImpl(this, applicative1);
    }

    @NotNull
    @Override
    default Iterator<E> iterator() {
       return isEmpty() ? Iterables.empty() : Iterables.singleton(this::get);
    }

    class Success<E> implements Try<E>, Unapply1<E> {

        private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Success.class);

        private final E value;

        static Pattern $Success(Pattern pattern) {
            return PATTERN.apply(pattern);
        }

        private Success(E value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public Exception getError() {
            throw new NoSuchElementException();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(get());
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Success && Objects.equals(get(), ((Success) obj).get());
        }

        @Override
        public String toString() {
            return "Success(" + get() + ")";
        }

        @Override
        public Product1<E> unapply() {
            return Tuple(value);
        }
    }

    class Failure<E> implements Try<E>, Unapply1<Throwable> {

        private final Throwable error;

        private static final Func1<Pattern, Pattern> PATTERN = PatternFactory.gen1(Failure.class);

        static Pattern $Failure(Pattern error) {
            return PATTERN.apply(error);
        }

        private Failure(Throwable error) {
            this.error = error;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public E get() {
            throw new RuntimeException("Error triggered by operation: " + error.getMessage(), error);
        }

        @Override
        public Throwable getError() {
            return error;
        }

        @SuppressWarnings("unchecked")
        private static <O, U> Failure<U> cast(Try<O> toCast) {
            return (Failure<U>) toCast;
        }

        @Override
        public String toString() {
            return "Failure(" + getError().getMessage() + ")";
        }

        @Override
        public Product1<Throwable> unapply() {
            return Tuple(error);
        }
    }
}
