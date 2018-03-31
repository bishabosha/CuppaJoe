package io.cuppajoe.typeclass.compose;

import io.cuppajoe.functions.Func5;

public interface Compose5<T1, T2, T3, T4, T5> {
    <O> O compose(Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends O> mapper);
}
