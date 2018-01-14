package com.bishabosha.cuppajoe;

import com.bishabosha.cuppajoe.control.Option;

import java.util.Optional;
import java.util.function.*;

import static com.bishabosha.cuppajoe.API.Nothing;
import static com.bishabosha.cuppajoe.API.Some;

public interface Value<O> extends Iterable<O> {
    boolean isEmpty();
    int size();
    O get();

    default O orElse(O value) {
        return isEmpty() ? value : get();
    }

    default O orElseGet(Supplier<? extends O> supplier) {
        return isEmpty() ? supplier.get() : get();
    }

    default <X extends Throwable> O orElseThrow(Supplier<? extends X> supplier) throws X {
        if (isEmpty()) {
            throw supplier.get();
        }
        return get();
    }

    default void ifEmpty(Runnable toDo) {
        if (isEmpty()) {
            toDo.run();
        }
    }

    default void ifSome(Consumer<? super O> toDo) {
        if (!isEmpty()) {
            toDo.accept(get());
        }
    }

    default void ifSomeOrElse(Consumer<? super O> toDo, Runnable alternative) {
        if (isEmpty()) {
            alternative.run();
        } else {
            toDo.accept(get());
        }
    }

    default Option<O> toOption() {
        return isEmpty() ? Nothing() : Some(get());
    }

    default Optional<O> toJavaOptional() {
        return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
    }
}
