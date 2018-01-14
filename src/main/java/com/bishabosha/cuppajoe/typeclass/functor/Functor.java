package com.bishabosha.cuppajoe.typeclass.functor;

import java.util.function.Function;

public interface Functor<INSTANCE, T> {
    <U> Functor<INSTANCE, U> map(Function<? super T, ? extends U> mapper);
}
