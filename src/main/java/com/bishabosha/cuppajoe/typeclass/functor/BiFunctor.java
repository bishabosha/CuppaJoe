package com.bishabosha.cuppajoe.typeclass.functor;

import com.bishabosha.cuppajoe.typeclass.base.Higher2;
import com.bishabosha.cuppajoe.typeclass.base.TypeClass;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BiFunctor<INSTANCE, T1, T2> extends TypeClass<BiFunctor, INSTANCE>, Higher2<TypeClass<BiFunctor, INSTANCE>, T1, T2> {
    <U1, U2> BiFunctor<INSTANCE, U1, U2> map(Function<? super T1, ? extends U1> m1, Function<? super T2, ? extends U2> m2);
    <U> U compose(BiFunction<? super T1, ? super T2, ? extends U> m2);
}
