package com.bishabosha.cuppajoe.typeclass;

import java.util.function.Function;

public interface Monad<INSTANCE, T> extends Applicative<INSTANCE, T> {
    <U> Monad<INSTANCE, U> flatMap(Function<? super T, Monad<INSTANCE, ? extends U>> mapper);
}
