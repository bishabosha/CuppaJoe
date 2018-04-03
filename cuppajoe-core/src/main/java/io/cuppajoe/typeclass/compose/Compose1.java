package io.cuppajoe.typeclass.compose;

import java.util.function.Function;

public interface Compose1<T1> {
    <O> O compose(Function<? super T1, ? extends O> mapper);
}
