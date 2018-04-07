package com.github.bishabosha.cuppajoe.typeclass.applicative;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.functions.*;
import com.github.bishabosha.cuppajoe.typeclass.functor.Functor1;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Applicative1<INSTANCE extends Applicative1, T> extends Functor1<INSTANCE, T> {
    <U> Applicative1<INSTANCE, U> pure(U value);

    <U> Applicative1<INSTANCE, U> apply(@NonNull Applicative1<INSTANCE, Function<? super T, ? extends U>> applicative1);

    static <X, R, INSTANCE extends Applicative1, XA extends Applicative1<INSTANCE, X>, RA extends Applicative1<INSTANCE, R>>
    Function<XA, RA> liftAFunc1(
            @NonNull Function<? super X, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return xA -> {
            Applicative1<INSTANCE, Function<? super X, ? extends R>> wrapped = xA.pure(function);
            return Type.narrow(xA.apply(wrapped));
        };
    }

    static <X, Y, R, INSTANCE extends Applicative1, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, RA extends Applicative1<INSTANCE, R>>
    BiFunction<XA, YA, RA> liftAFunc2(
            @NonNull BiFunction<? super X, ? super Y, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (xA, yA) -> {
            Applicative1<INSTANCE, Function<? super X, ? extends Function<? super Y, ? extends R>>> curried =
                    xA.pure(Func2.of(function).curried());
            return Type.narrow(yA.apply(xA.apply(curried)));
        };
    }

    static <X, Y, Z, R, INSTANCE extends Applicative1, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func3<XA, YA, ZA, RA> liftAFunc3(
            @NonNull Func3<? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>> curried =
                    xA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(curried))));
        };
    }

    static <W, X, Y, Z, R, INSTANCE extends Applicative1, WA extends Applicative1<INSTANCE, W>, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func4<WA, XA, YA, ZA, RA> liftAFunc4(
            @NonNull Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (wA, xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super W, ? extends Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>>> curried =
                    wA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(wA.apply(curried)))));
        };
    }

    static <V, W, X, Y, Z, R, INSTANCE extends Applicative1, VA extends Applicative1<INSTANCE, V>, WA extends Applicative1<INSTANCE, W>, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func5<VA, WA, XA, YA, ZA, RA> liftAFunc5(
            @NonNull Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (vA, wA, xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super V, ? extends Function<? super W, ? extends Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>>>> curried =
                    vA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(wA.apply(vA.apply(curried))))));
        };
    }

    static <U, V, W, X, Y, Z, R, INSTANCE extends Applicative1, UA extends Applicative1<INSTANCE, U>, VA extends Applicative1<INSTANCE, V>, WA extends Applicative1<INSTANCE, W>, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func6<UA, VA, WA, XA, YA, ZA, RA> liftAFunc6(
            @NonNull Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (uA, vA, wA, xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super U, ? extends Function<? super V, ? extends Function<? super W, ? extends Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>>>>> curried =
                    uA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(wA.apply(vA.apply(uA.apply(curried)))))));
        };
    }

    static <T, U, V, W, X, Y, Z, R, INSTANCE extends Applicative1, TA extends Applicative1<INSTANCE, T>, UA extends Applicative1<INSTANCE, U>, VA extends Applicative1<INSTANCE, V>, WA extends Applicative1<INSTANCE, W>, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func7<TA, UA, VA, WA, XA, YA, ZA, RA> liftAFunc7(
            @NonNull Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (tA, uA, vA, wA, xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super T, ? extends Function<? super U, ? extends Function<? super V, ? extends Function<? super W, ? extends Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>>>>>> curried =
                    tA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(wA.apply(vA.apply(uA.apply(tA.apply(curried))))))));
        };
    }

    static <S, T, U, V, W, X, Y, Z, R, INSTANCE extends Applicative1, SA extends Applicative1<INSTANCE, S>, TA extends Applicative1<INSTANCE, T>, UA extends Applicative1<INSTANCE, U>, VA extends Applicative1<INSTANCE, V>, WA extends Applicative1<INSTANCE, W>, XA extends Applicative1<INSTANCE, X>, YA extends Applicative1<INSTANCE, Y>, ZA extends Applicative1<INSTANCE, Z>, RA extends Applicative1<INSTANCE, R>>
    Func8<SA, TA, UA, VA, WA, XA, YA, ZA, RA> liftAFunc8(
            @NonNull Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function, "function");
        return (sA, tA, uA, vA, wA, xA, yA, zA) -> {
            Applicative1<INSTANCE, Function<? super S, ? extends Function<? super T, ? extends Function<? super U, ? extends Function<? super V, ? extends Function<? super W, ? extends Function<? super X, ? extends Function<? super Y, ? extends Function<? super Z, ? extends R>>>>>>>>> curried =
                    sA.pure(function.curried());
            return Type.narrow(zA.apply(yA.apply(xA.apply(wA.apply(vA.apply(uA.apply(tA.apply(sA.apply(curried)))))))));
        };
    }

    interface Type {

        @SuppressWarnings("unchecked")
        static <OUT extends Applicative1<INSTANCE, U>, INSTANCE extends Applicative1, U> OUT narrow(Applicative1<INSTANCE, ? extends U> higher) {
            return (OUT) higher;
        }
    }
}
