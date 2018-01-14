package com.bishabosha.cuppajoe.typeclass.functor;

import com.bishabosha.cuppajoe.typeclass.base.Higher;
import com.bishabosha.cuppajoe.typeclass.base.TypeClass;

import java.util.function.Function;

public interface Functor<INSTANCE, T> extends TypeClass<Functor, INSTANCE>, Higher<TypeClass<Functor, INSTANCE>, T> {
    <U> Functor<INSTANCE, U> map(Function<? super T, ? extends U> mapper);
}
