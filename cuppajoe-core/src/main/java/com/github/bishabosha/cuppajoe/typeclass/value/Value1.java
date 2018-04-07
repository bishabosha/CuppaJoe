package com.github.bishabosha.cuppajoe.typeclass.value;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;

public interface Value1<INSTANCE extends Value1, E> extends Iterable<E> {
    boolean isEmpty();

    E get();

    default boolean containsValue() {
        return !isEmpty();
    }

    Value1<INSTANCE, E> or(@NonNull Supplier<? extends Value1<INSTANCE, ? extends E>> alternative);

    default E orElse(E alternative) {
        return isEmpty() ? alternative : get();
    }

    default E orElseSupply(@NonNull Supplier<? extends E> supplier) {
        Objects.requireNonNull(supplier, "supplier");
        return isEmpty() ? supplier.get() : this.get();
    }

    default <X extends Throwable> E orElseThrow(@NonNull Supplier<? extends X> ifNothing) throws X {
        Objects.requireNonNull(ifNothing, "ifNothing");
        ifEmptyThrow(ifNothing);
        return get();
    }

    default <X extends Throwable> void ifEmptyThrow(@NonNull Supplier<? extends X> ifNothing) throws X {
        Objects.requireNonNull(ifNothing, "ifNothing");
        if (isEmpty()) {
            throw ifNothing.get();
        }
    }

    default void ifEmpty(@NonNull Runnable toDo) {
        if (isEmpty()) {
            toDo.run();
        }
    }

    default void doIfSomeElse(@NonNull Consumer<? super E> ifSomething, @NonNull Runnable ifNothing) {
        Objects.requireNonNull(ifSomething, "ifSomething");
        Objects.requireNonNull(ifNothing, "ifNothing");
        if (isEmpty()) {
            ifNothing.run();
        } else {
            ifSomething.accept(get());
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
