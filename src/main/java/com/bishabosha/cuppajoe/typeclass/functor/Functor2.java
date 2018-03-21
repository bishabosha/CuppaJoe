package com.bishabosha.cuppajoe.typeclass.functor;

import java.util.function.Function;

public interface Functor2<INSTANCE extends Functor2, T1, T2> {
    <U1, U2> Functor2<INSTANCE, U1, U2> map(Function<? super T1, ? extends U1> m1, Function<? super T2, ? extends U2> m2);
}
