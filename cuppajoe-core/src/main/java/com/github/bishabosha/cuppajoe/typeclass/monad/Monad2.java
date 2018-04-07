package com.github.bishabosha.cuppajoe.typeclass.monad;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.typeclass.applicative.Applicative2;
import com.github.bishabosha.cuppajoe.typeclass.functor.Functor2;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Monad2<INSTANCE extends Monad2, T1, T2> extends Applicative2<INSTANCE, T1, T2> {

    <U1, U2> Monad2<INSTANCE, U1, U2> flatMap(BiFunction<? super T1, ? super T2, Monad2<INSTANCE, ? extends U1, ? extends U2>> mapper);

    default <U1, U2> Monad2<INSTANCE, U1, U2> bless(U1 a, U2 b) {
        return Type.narrowA(pure(a, b));
    }

    static <X, Y, INSTANCE extends Monad2, FINAL extends Monad2<INSTANCE, X, Y>> UnaryOperator<FINAL> m1Op(@NonNull UnaryOperator<X> operator1, @NonNull UnaryOperator<Y> operator2) {
        Objects.requireNonNull(operator1, "operator1");
        Objects.requireNonNull(operator2, "operator2");
        return x -> Monad2.Type.narrow(x.flatMap((a, b) -> x.bless(operator1.apply(a), operator2.apply(b))));
    }


    static <X, Y, INSTANCE extends Monad2, FINAL extends Monad2<INSTANCE, X, Y>> BinaryOperator<FINAL> m2Op(@NonNull BinaryOperator<X> operator1, @NonNull BinaryOperator<Y> operator2) {
        Objects.requireNonNull(operator1, "operator1");
        Objects.requireNonNull(operator2, "operator2");
        return (x, y) -> Monad2.Type.narrow(x.flatMap((x1, y1) -> y.flatMap((x2, y2) -> y.bless(operator1.apply(x1, x2), operator2.apply(y1, y2)))));
    }


    static <X, U1, Y, U2, INSTANCE extends Monad2, IM extends Monad2<INSTANCE, X, Y>, OM extends Monad2<INSTANCE, U1, U2>>
    Function<IM, OM> m1(@NonNull Function<? super X, ? extends U1> function1, @NonNull Function<? super Y, ? extends U2> function2) {
        Objects.requireNonNull(function1, "function1");
        Objects.requireNonNull(function2, "function2");
        return x -> Monad2.Type.narrow(x.flatMap((x1, y1) -> x.bless((U1) function1.apply(x1), (U2) function2.apply(y1))));
    }


    static <X1, X2, U1, Y1, Y2, U2, INSTANCE extends Monad2, IM1 extends Monad2<INSTANCE, X1, Y1>, IM2 extends Monad2<INSTANCE, X2, Y2>, OM extends Monad2<INSTANCE, U1, U2>>
    BiFunction<IM1, IM2, OM> m2(@NonNull BiFunction<? super X1, ? super X2, ? extends U1> function1, @NonNull BiFunction<? super Y1, ? super Y2, ? extends U2> function2) {
        Objects.requireNonNull(function1, "function1");
        Objects.requireNonNull(function2, "function2");
        return (x, y) -> Monad2.Type.narrow(x.flatMap((x1, y1) -> y.flatMap((x2, y2) -> y.bless((U1) function1.apply(x1, x2), (U2) function2.apply(y1, y2)))));
    }

    interface Type {

        @SuppressWarnings("unchecked")
        static <FINAL extends Monad2<INSTANCE, U1, U2>, INSTANCE extends Monad2, U1, U2> FINAL narrowA(Applicative2<INSTANCE, ? extends U1, ? extends U2> monad) {
            return (FINAL) monad;
        }

        @SuppressWarnings("unchecked")
        static <FINAL extends Monad2<INSTANCE, U1, U2>, INSTANCE extends Monad2, U1, U2> FINAL narrowF(Functor2<INSTANCE, ? extends U1, ? extends U2> monad) {
            return (FINAL) monad;
        }

        @SuppressWarnings("unchecked")
        static <FINAL extends Monad2<INSTANCE, U1, U2>, INSTANCE extends Monad2, U1, U2> FINAL narrow(Monad2<INSTANCE, ? extends U1, ? extends U2> monad) {
            return (FINAL) monad;
        }
    }
}
