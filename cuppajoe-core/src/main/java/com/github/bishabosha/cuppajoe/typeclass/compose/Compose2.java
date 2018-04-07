package com.github.bishabosha.cuppajoe.typeclass.compose;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.function.BiFunction;

public interface Compose2<T1, T2> {
    <O> O compose(@NonNull BiFunction<? super T1, ? super T2, ? extends O> mapper);
}
