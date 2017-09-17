package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.control.Option;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Some;

public interface Value<O> extends Iterable<O> {
    boolean isEmpty();
    boolean isAtMaxSingleElement();
    O get();
    <R> Value<R> map(Function<? super O, ? extends R> mapper);

    default boolean contains(O obj) {
        return anyMatch(e -> Objects.equals(e, obj));
    }

    default O orElse(O value) {
        return isEmpty() ? value : get();
    }

    default O orElseGet(Supplier<? extends O> supplier) {
        return isEmpty() ? supplier.get() : get();
    }

    default O orElseNull() {
        return isEmpty() ? null : get();
    }

    default <X extends Throwable> O orElseThrow(Supplier<? extends X> supplier) throws X {
        if (isEmpty()) {
            throw supplier.get();
        }
        return get();
    }

    default <R> boolean allMatch(Iterable<R> other, BiPredicate<? super O, ? super R> test) {
        Iterator<R> otherVals = other.iterator();
        for (O elem: this) {
            if (!otherVals.hasNext() || !test.test(elem, otherVals.next())) {
                return false;
            }
        }
        return true;
    }

    default boolean allMatch(Predicate<? super O> test) {
        for (O elem: this) {
            if (!test.test(elem)) {
                return false;
            }
        }
        return true;
    }

    default boolean anyMatch(Predicate<? super O> test) {
        for (O elem: this) {
            if (test.test(elem)) {
                return true;
            }
        }
        return false;
    }

    default <R> boolean anyMatch(Iterable<R> other, BiPredicate<? super O, ? super R> test) {
        Iterator<R> otherVals = other.iterator();
        for (O elem: this) {
            if (!otherVals.hasNext()) {
                return false;
            }
            if (test.test(elem, otherVals.next())) {
                return true;
            }
        }
        return false;
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
