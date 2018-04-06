package io.cuppajoe.typeclass.compose;

import io.cuppajoe.annotation.NonNull;

import java.util.function.Function;

public interface Compose1<T1> {
    <O> O compose(@NonNull Function<? super T1, ? extends O> mapper);
}
