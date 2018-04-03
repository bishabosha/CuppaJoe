package io.cuppajoe.typeclass.compose;

import io.cuppajoe.functions.Func4;

public interface Compose4<T1, T2, T3, T4> {
    <O> O compose(Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends O> mapper);
}
