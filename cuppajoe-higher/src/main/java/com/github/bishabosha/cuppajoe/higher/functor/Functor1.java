package com.github.bishabosha.cuppajoe.higher.functor;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.function.Function;

public interface Functor1<INSTANCE extends Functor1, T> {
    <U> Functor1<INSTANCE, U> map(@NonNull Function<? super T, ? extends U> mapper);

    interface Type {

        @SuppressWarnings("unchecked")
        static <OUT extends Functor1<INSTANCE, U>, INSTANCE extends Functor1, U> OUT castParam(Functor1<INSTANCE, ?> higher) {
            return (OUT) higher;
        }
    }
}
