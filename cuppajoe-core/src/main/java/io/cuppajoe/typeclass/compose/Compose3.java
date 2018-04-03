package io.cuppajoe.typeclass.compose;

import io.cuppajoe.functions.Func3;

public interface Compose3<T1, T2, T3> {
    <O> O compose(Func3<? super T1, ? super T2, ? super T3, ? extends O> mapper);
}
