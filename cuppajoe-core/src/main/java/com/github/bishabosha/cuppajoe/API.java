package com.github.bishabosha.cuppajoe;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.higher.functions.CheckedFunc0;
import com.github.bishabosha.cuppajoe.higher.functions.TailCall;
import com.github.bishabosha.cuppajoe.tuples.*;

import java.util.function.Supplier;

public final class API {

    private API() {
    }

    public static Unit
    Tuple() {
        return Tuple.of();
    }

    public static <A>
    Tuple1<A>
    Tuple(A $1) {
        return Tuple.of($1);
    }

    public static <A, B>
    Tuple2<A, B>
    Tuple(A $1, B $2) {
        return Tuple.of($1, $2);
    }

    public static <A, B, C>
    Tuple3<A, B, C>
    Tuple(A $1, B $2, C $3) {
        return Tuple.of($1, $2, $3);
    }

    public static <A, B, C, D>
    Tuple4<A, B, C, D>
    Tuple(A $1, B $2, C $3, D $4) {
        return Tuple.of($1, $2, $3, $4);
    }

    public static <A, B, C, D, E>
    Tuple5<A, B, C, D, E>
    Tuple(A $1, B $2, C $3, D $4, E $5) {
        return Tuple.of($1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F>
    Tuple6<A, B, C, D, E, F>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6) {
        return Tuple.of($1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G>
    Tuple7<A, B, C, D, E, F, G>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return Tuple.of($1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return Tuple.of($1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static <L, R>
    Either.Left<L, R>
    Left(L left) {
        return Either.left(left);
    }

    public static <L, R>
    Either.Right<L, R>
    Right(R right) {
        return Either.right(right);
    }

    public static <O>
    Option<O>
    Some(O elem) {
        return Option.some(elem);
    }

    public static <O>
    Option<O>
    None() {
        return Option.empty();
    }

    public static <O>
    Lazy<O>
    Lazy(@NonNull Supplier<O> getter) {
        return Lazy.of(getter);
    }

    public static <O>
    Try<O>
    Try(@NonNull CheckedFunc0<O> getter) {
        return Try.of(getter);
    }

    public static <O>
    Try<O>
    Success(O value) {
        return Try.success(value);
    }

    public static <O>
    Try<O>
    Failure(@NonNull Exception error) {
        return Try.failure(error);
    }

    public static <O>
    TailCall<O>
    Call(@NonNull Supplier<TailCall<O>> tailCall) {
        return TailCall.call(tailCall);
    }

    public static <O>
    TailCall<O>
    Yield(O result) {
        return TailCall.yield(result);
    }
}
