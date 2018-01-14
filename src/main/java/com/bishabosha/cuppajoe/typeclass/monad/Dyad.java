package com.bishabosha.cuppajoe.typeclass.monad;

import com.bishabosha.cuppajoe.typeclass.applicative.BiApplicative;

import java.util.function.BiFunction;

public interface Dyad<INSTANCE, T1, T2> extends BiApplicative<INSTANCE, T1, T2> {
    <U1, U2> Dyad<INSTANCE, U1, U2> flatMap(BiFunction<? super T1, ? super T2, Dyad<INSTANCE, ? extends U1, ? extends U2>> mapper);
}
