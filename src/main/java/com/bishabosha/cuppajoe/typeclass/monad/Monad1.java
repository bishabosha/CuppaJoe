package com.bishabosha.cuppajoe.typeclass.monad;

import com.bishabosha.cuppajoe.functions.*;
import com.bishabosha.cuppajoe.typeclass.applicative.Applicative1;
import com.bishabosha.cuppajoe.typeclass.functor.Functor1;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Monad1<INSTANCE extends Monad1, T> extends Applicative1<INSTANCE, T> {

    <U> Monad1<INSTANCE, U> flatMap(Function<? super T, Monad1<INSTANCE, ? extends U>> mapper);

    default <U> Monad1<INSTANCE, U> bless(U value) {
        return Type.lA(pure(value));
    }

    static <X, R, INSTANCE extends Monad1, RA extends Monad1<INSTANCE, R>> RA applyImpl(Monad1<INSTANCE, X> m,
            Applicative1<INSTANCE, Function<? super X, ? extends R>> applicative) {
        Objects.requireNonNull(applicative);
        return Type.narrow(Type.lA(applicative).flatMap(xM -> Type.<INSTANCE, R>lF(m.map(y -> xM.apply(y)))));
    }

    static <U, INSTANCE extends Monad1, FINAL extends Monad1<INSTANCE, U>>
    UnaryOperator<FINAL> liftMOp1(
            UnaryOperator<U> operator) {
        Objects.requireNonNull(operator);
        return xM -> Type.narrow(xM.flatMap(a -> xM.bless(operator.apply(a))));
    }

    static <U, INSTANCE extends Monad1, FINAL extends Monad1<INSTANCE, U>>
    BinaryOperator<FINAL> liftMOp2(
            BinaryOperator<U> operator) {
        Objects.requireNonNull(operator);
        return (xM, yM) -> Type.narrow(xM.flatMap(a -> yM.flatMap(b -> yM.bless(operator.apply(a, b)))));
    }

    static <X, R, INSTANCE extends Monad1, XM extends Monad1<INSTANCE, X>, RM extends Monad1<INSTANCE, R>>
    Function<XM, RM> liftMFunc1(
            Function<? super X, ? extends R> function) {
        Objects.requireNonNull(function);
        return xM -> Type.narrow(Type.lF(xM.map(
            x -> (R) function.apply(x))));
    }

    static <X, Y, R, INSTANCE extends Monad1, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, RM extends Monad1<INSTANCE, R>>
    BiFunction<XM, YM, RM> liftMFunc2(
            BiFunction<? super X, ? super Y, ? extends R> function) {
        Objects.requireNonNull(function);
        return (xM, yM) -> Type.narrow(xM.flatMap(
            x -> Type.lF(yM.map(
                y -> (R) function.apply(x, y)))));
    }

    static <X, Y, Z, R, INSTANCE extends Monad1, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func3<XM, YM, ZM, RM> liftMFunc3(
            Func3<? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (xM, yM, zM) -> Type.narrow(xM.flatMap(
            x -> yM.flatMap(
                y -> Type.lF(zM.map(
                    z -> (R) function.apply(x, y, z))))));
    }

    static <W, X, Y, Z, R, INSTANCE extends Monad1, WM extends Monad1<INSTANCE, W>, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func4<WM, XM, YM, ZM, RM> liftMFunc4(
            Func4<? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (wM, xM, yM, zM) -> Type.narrow(wM.flatMap(
            w -> xM.flatMap(
                x -> yM.flatMap(
                    y -> Type.lF(zM.map(
                        z -> (R) function.apply(w, x, y, z)))))));
    }

    static <V, W, X, Y, Z, R, INSTANCE extends Monad1, VM extends Monad1<INSTANCE, V>, WM extends Monad1<INSTANCE, W>, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func5<VM, WM, XM, YM, ZM, RM> liftMFunc5(
            Func5<? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (vM, wM, xM, yM, zM) -> Type.narrow(vM.flatMap(
            v -> wM.flatMap(
                w -> xM.flatMap(
                    x -> yM.flatMap(
                        y -> Type.lF(zM.map(
                            z -> (R) function.apply(v, w, x, y, z))))))));
    }

    static <U, V, W, X, Y, Z, R, INSTANCE extends Monad1, UM extends Monad1<INSTANCE, U>, VM extends Monad1<INSTANCE, V>, WM extends Monad1<INSTANCE, W>, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func6<UM, VM, WM, XM, YM, ZM, RM> liftMFunc6(
            Func6<? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (uM, vM, wM, xM, yM, zM) -> Type.narrow(uM.flatMap(
            u -> vM.flatMap(
                v -> wM.flatMap(
                    w -> xM.flatMap(
                        x -> yM.flatMap(
                            y -> Type.lF(zM.map(
                                z -> (R) function.apply(u, v, w, x, y, z)))))))));
    }

    static <T, U, V, W, X, Y, Z, R, INSTANCE extends Monad1, TM extends Monad1<INSTANCE, T>, UM extends Monad1<INSTANCE, U>, VM extends Monad1<INSTANCE, V>, WM extends Monad1<INSTANCE, W>, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func7<TM, UM, VM, WM, XM, YM, ZM, RM> liftMFunc7(
            Func7<? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (tM, uM, vM, wM, xM, yM, zM) -> Type.narrow(tM.flatMap(
            t -> uM.flatMap(
                u -> vM.flatMap(
                    v -> wM.flatMap(
                        w -> xM.flatMap(
                            x -> yM.flatMap(
                                y -> Type.lF(zM.map(
                                    z -> (R) function.apply(t, u, v, w, x, y, z))))))))));
    }

    static <S, T, U, V, W, X, Y, Z, R, INSTANCE extends Monad1, SM extends Monad1<INSTANCE, S>, TM extends Monad1<INSTANCE, T>, UM extends Monad1<INSTANCE, U>, VM extends Monad1<INSTANCE, V>, WM extends Monad1<INSTANCE, W>, XM extends Monad1<INSTANCE, X>, YM extends Monad1<INSTANCE, Y>, ZM extends Monad1<INSTANCE, Z>, RM extends Monad1<INSTANCE, R>>
    Func8<SM, TM, UM, VM, WM, XM, YM, ZM, RM> liftMFunc8(
            Func8<? super S, ? super T, ? super U, ? super V, ? super W, ? super X, ? super Y, ? super Z, ? extends R> function) {
        Objects.requireNonNull(function);
        return (sM, tM, uM, vM, wM, xM, yM, zM) -> Type.narrow(sM.flatMap(
            s -> tM.flatMap(
                t -> uM.flatMap(
                    u -> vM.flatMap(
                        v -> wM.flatMap(
                            w -> xM.flatMap(
                                x -> yM.flatMap(
                                    y -> Type.lF(zM.map(
                                        z -> (R) function.apply(s, t, u, v, w, x, y, z)))))))))));
    }

    interface Type {

        @SuppressWarnings("unchecked")
        static <INSTANCE extends Monad1, U> Monad1<INSTANCE, U> lF(Functor1<INSTANCE, U> functor1) {
            return (Monad1<INSTANCE, U>) functor1;
        }

        @SuppressWarnings("unchecked")
        static <INSTANCE extends Monad1, U> Monad1<INSTANCE, U> lA(Applicative1<INSTANCE, U> applicative1) {
            return (Monad1<INSTANCE, U>) applicative1;
        }

        @SuppressWarnings("unchecked")
        static <OUT extends Monad1<INSTANCE, U>, INSTANCE extends Monad1, U> OUT narrow(Monad1<INSTANCE, ? extends U> higher) {
            return (OUT) higher;
        }

        @SuppressWarnings("unchecked")
        static <OUT extends Monad1<INSTANCE, U>, INSTANCE extends Monad1, U> OUT castParam(Monad1<INSTANCE, ?> higher) {
            return (OUT) higher;
        }
    }
}
