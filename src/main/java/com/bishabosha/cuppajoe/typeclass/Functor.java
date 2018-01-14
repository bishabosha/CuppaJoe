package com.bishabosha.cuppajoe.typeclass;

import java.util.function.Function;

public interface Functor<INSTANCE, T> extends TypeClass {
    <U> Functor<INSTANCE, U> map(Function<? super T, ? extends U> mapper);
}
