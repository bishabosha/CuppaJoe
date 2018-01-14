package com.bishabosha.cuppajoe.typeclass.applicative;

import com.bishabosha.cuppajoe.functions.*;
import com.bishabosha.cuppajoe.typeclass.functor.Functor;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Applicative<INSTANCE, T> extends Functor<INSTANCE, T> {
    Applicative<INSTANCE, T> pure(T value);
    <U> Applicative<INSTANCE, U> apply(Applicative<INSTANCE, Function<? super T, ? extends U>> applicative);
    <T, S> Func1<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, S>> lift(Function<T, S> function);
    <T, U, S> Func2<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, S>> lift2(BiFunction<T, U, S> function);
    <T, U, V, S> Func3<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, S>> lift3(Func3<T, U, V, S> function);
    <T, U, V, M, S> Func4<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, M>, ? extends Applicative<INSTANCE, S>> lift4(Func4<T, U, V, M, S> function);
    <T, U, V, M, R, S> Func5<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, M>, ? extends Applicative<INSTANCE, R>, ? extends Applicative<INSTANCE, S>> lift5(Func5<T, U, V, M, R, S> function);
    <T, U, V, M, R, D, S> Func6<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, M>, ? extends Applicative<INSTANCE, R>, ? extends Applicative<INSTANCE, D>, ? extends Applicative<INSTANCE, S>> lift6(Func6<T, U, V, M, R, D, S> function);
    <T, U, V, M, R, D, Q, S> Func7<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, M>, ? extends Applicative<INSTANCE, R>, ? extends Applicative<INSTANCE, D>, ? extends Applicative<INSTANCE, Q>, ? extends Applicative<INSTANCE, S>> lift7(Func7<T, U, V, M, R, D, Q, S> function);
    <T, U, V, M, R, D, Q, F, S> Func8<? extends Applicative<INSTANCE, T>, ? extends Applicative<INSTANCE, U>, ? extends Applicative<INSTANCE, V>, ? extends Applicative<INSTANCE, M>, ? extends Applicative<INSTANCE, R>, ? extends Applicative<INSTANCE, D>, ? extends Applicative<INSTANCE, Q>, ? extends Applicative<INSTANCE, F>, ? extends Applicative<INSTANCE, S>> lift8(Func8<T, U, V, M, R, D, Q, F, S> function);
}
