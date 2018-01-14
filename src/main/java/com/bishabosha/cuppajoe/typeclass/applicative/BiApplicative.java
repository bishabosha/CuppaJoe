package com.bishabosha.cuppajoe.typeclass.applicative;

import com.bishabosha.cuppajoe.typeclass.functor.BiFunctor;

import java.util.function.Function;

public interface BiApplicative<INSTANCE, T1, T2> extends BiFunctor<INSTANCE, T1, T2> {
    <U1, U2> BiApplicative<INSTANCE, U1, U2> apply(BiApplicative<INSTANCE, Function<? super T1, ? extends U1>, Function<? super T2, ? extends U2>> applicative);
}
