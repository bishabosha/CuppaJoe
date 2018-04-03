package io.cuppajoe.typeclass.value;

import io.cuppajoe.control.Option;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Some;

public interface Value1<INSTANCE extends Value1, E> extends Iterable<E> {
    boolean isEmpty();
    E get();

    default boolean containsValue() {
        return !isEmpty();
    }

    Value1<INSTANCE, E> or(Supplier<? extends Value1<INSTANCE, ? extends E>> alternative);

    default E orElse(E alternative) {
        return isEmpty() ? alternative : get();
    }

    default E orElseSupply(Supplier<? extends E> supplier) {
        Objects.requireNonNull(supplier);
        return isEmpty() ? supplier.get() : this.get();
    }

    default <X extends Throwable> E orElseThrow(Supplier<? extends X> ifNothing) throws X {
        ifEmptyThrow(ifNothing);
        return get();
    }

    default <X extends Throwable> void ifEmptyThrow(Supplier<? extends X> ifNothing) throws X {
        Objects.requireNonNull(ifNothing);
        if (isEmpty()) {
            throw ifNothing.get();
        }
    }

    default void ifEmpty(Runnable toDo) {
        if (isEmpty()) {
            toDo.run();
        }
    }

    default void doIfSomeElse(Consumer<? super E> toDo, Runnable alternative) {
        if (isEmpty()) {
            alternative.run();
        } else {
            toDo.accept(get());
        }
    }

    default Option<E> toOption() {
        return isEmpty() ? None() : Some(get());
    }

    default Optional<E> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    interface Type {
        static <OUT extends Value1<INSTANCE, U>, INSTANCE extends Value1, U> OUT narrow(Value1<INSTANCE, ? extends U> higher) {
            return (OUT) higher;
        }
    }
}
