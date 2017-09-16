package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.control.*;
import com.bishabosha.caffeine.functional.functions.CheckedFunc0;
import com.bishabosha.caffeine.functional.functions.Func0;
import com.bishabosha.caffeine.functional.patterns.Case;
import com.bishabosha.caffeine.functional.patterns.Matcher;
import com.bishabosha.caffeine.functional.tuples.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class API {

    public static Unit
    Tuple() {
        return Unit.getInstance();
    }

    public static <A>
    Tuple1<A>
    Tuple(A $1) {
        return Tuple1.of($1);
    }

    public static <A, B>
    Tuple2<A, B>
    Tuple(A $1, B $2) {
        return Tuple2.of($1, $2);
    }

    public static <A, B, C>
    Tuple3<A, B, C> Tuple(A $1, B $2, C $3) {
        return Tuple3.of($1, $2, $3);
    }

    public static <A, B, C, D>
    Tuple4<A, B, C, D>
    Tuple(A $1, B $2, C $3, D $4) {
        return Tuple4.of($1, $2, $3, $4);
    }

    public static <A, B, C, D, E>
    Tuple5<A, B, C, D, E>
    Tuple(A $1, B $2, C $3, D $4, E $5) {
        return Tuple5.of($1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F>
    Tuple6<A, B, C, D, E, F>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6) {
        return Tuple6.of($1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G>
    Tuple7<A, B, C, D, E, F, G>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return Tuple7.of($1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return Tuple8.of($1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static <L, R> Either<L, R> Left(L left) {
        return Either.Left.of(left);
    }

    public static <L, R> Either<L, R> Right(R right) {
        return Either.Right.of(right);
    }

    @Contract(pure = true)
    @NotNull
    public static <O> Option<O> Option(O elem) {
        return Option.of(elem);
    }

    public static <O> Option<O> Option(BooleanSupplier condition, Func0<O> elem) {
        return condition.getAsBoolean() ? Option(elem.apply()) : Nothing();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Option<O> Some(O elem) {
        return Some.of(elem);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Option<O> Nothing() {
        return Nothing.getInstance();
    }

    @NotNull
    public static <O> Try<O> Try(Func0<O> getter) {
        return Try.of(getter);
    }

    @NotNull
    public static <O> Try<O> Try(CheckedFunc0<O> getter) {
        return Try.of(getter);
    }

    @NotNull
    public static <I> Matcher<I> Match(I toMatch) {
        return Matcher.create(toMatch);
    }

    public static <I, O> Option<O> Match(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isSome() ? options.match(toMatch.get()) : Nothing();
    }
}
