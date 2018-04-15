package com.github.bishabosha.cuppajoe;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.higher.functions.CheckedFunc0;
import com.github.bishabosha.cuppajoe.higher.functions.TailCall;

import java.util.function.Supplier;

public final class API {

    private API() {
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
