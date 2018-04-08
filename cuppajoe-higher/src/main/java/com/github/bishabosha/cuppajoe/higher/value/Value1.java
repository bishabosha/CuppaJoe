package com.github.bishabosha.cuppajoe.higher.value;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.value.internal.value1.Box;
import com.github.bishabosha.cuppajoe.higher.value.internal.value1.Empty;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Value1<INSTANCE extends Value1, E> extends Iterable<E> {

    default Value1<INSTANCE, E> or(@NonNull Supplier<? extends Value1<INSTANCE, ? extends E>> alternative) {
        return Value1.Type.narrow(alternative.get());
    }

    default boolean isEmpty() {
        return true;
    }

    default boolean containsValue() {
        return !isEmpty();
    }

    default E get() {
        throw new NoSuchElementException();
    }

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

    default Optional<E> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }

    @Override
    default Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    static <O> Value<O> value(O value) {
        return new Box<>(value);
    }

    static <O> Value<O> empty() {
        return Value1.Type.<Value<O>, Value, O>castParam(Empty.INSTANCE);
    }

    interface Value<E> extends Value1<Value, E> {
    }

    interface Type {

        @SuppressWarnings("unchecked")
        static <OUT extends Value1<INSTANCE, U>, INSTANCE extends Value1, U> OUT narrow(Value1<INSTANCE, ? extends U> higher) {
            return (OUT) higher;
        }

        @SuppressWarnings("unchecked")
        static <OUT extends Value1<INSTANCE, U>, INSTANCE extends Value1, U> OUT castParam(Value1<INSTANCE, ?> higher) {
            return (OUT) higher;
        }
    }
}

