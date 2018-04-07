package com.github.bishabosha.cuppajoe.typeclass.foldable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.Objects;
import java.util.function.BiFunction;

public interface Foldable<E> extends Iterable<E> {
    default <O> O foldLeft(O accumulator, @NonNull BiFunction<O, E, O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return foldOver(this, accumulator, mapper);
    }

    <O> O foldRight(O accumulator, @NonNull BiFunction<O, E, O> mapper);

    static <E, A> A foldOver(@NonNull Iterable<E> that, A accumulator, @NonNull BiFunction<A, E, A> mapper) {
        Objects.requireNonNull(that, "that");
        Objects.requireNonNull(mapper, "mapper");
        for (var elem : that) {
            accumulator = mapper.apply(accumulator, elem);
        }
        return accumulator;
    }
}
