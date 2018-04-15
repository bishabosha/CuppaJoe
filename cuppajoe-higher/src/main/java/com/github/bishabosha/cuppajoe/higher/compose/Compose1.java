package com.github.bishabosha.cuppajoe.higher.compose;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.function.Function;

public interface Compose1<T1> {
    <O> O compose(@NonNull Function<? super T1, ? extends O> mapper);
}
