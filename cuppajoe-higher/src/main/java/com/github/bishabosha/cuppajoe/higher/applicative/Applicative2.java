package com.github.bishabosha.cuppajoe.higher.applicative;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.higher.functor.Functor2;

import java.util.function.Function;

public interface Applicative2<INSTANCE extends Applicative2, T1, T2> extends Functor2<INSTANCE, T1, T2> {
    <U1, U2> Applicative2<INSTANCE, U1, U2> pure(U1 a, U2 b);

    <U1, U2> Applicative2<INSTANCE, U1, U2> apply(@NonNull Applicative2<INSTANCE, Function<? super T1, ? extends U1>, Function<? super T2, ? extends U2>> applicative);
}
