package io.cuppajoe;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.Queue;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.control.Either;
import io.cuppajoe.control.Either.Left;
import io.cuppajoe.control.Either.Right;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Try;
import io.cuppajoe.functions.CheckedFunc0;
import io.cuppajoe.functions.Func0;
import io.cuppajoe.functions.TailCall;
import io.cuppajoe.tuples.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class API {

    public static
    Unit
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

    public static <L, R> Left<L, R> Left(L left) {
        return Either.left(left);
    }

    public static <L, R> Right<L, R> Right(R right) {
        return Either.right(right);
    }

    public static <O> Option<O> Option(BooleanSupplier condition, Func0<O> elem) {
        return condition.getAsBoolean() ? Option.of(elem.get()) : None();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Option<O> Some(@Nullable O elem) {
        return Option.some(elem);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Option<O> None() {
        return Option.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Lazy<O> Lazy(Supplier<O> getter) {
        return Lazy.of(getter);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Try<O> Try(CheckedFunc0<O> getter) {
        return Try.of(getter);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Try<O> Success(@Nullable O value) {
        return Try.success(value);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Try<O> Failure(Exception error) {
        return Try.failure(error);
    }

    @NotNull
    @Contract(pure = true)
    public static <O extends Comparable<O>> Tree<O> Tree(O... values) {
        return Tree.of(values);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List() {
        return List.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a) {
        return List.concat(a, List.empty());
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b) {
        return List.concat(a, List.of(b));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c) {
        return List.concat(a, List.concat(b, List.of(c)));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d) {
        return List.concat(a, List.concat(b, List.concat(c, List.of(d))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.of(e)))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.of(f))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.of(g)))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g, O h) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.concat(g, List.of(h))))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O... elems) {
        return List.of(elems);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue() {
        return Queue.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue(O elem) {
        return Queue.of(elem);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue(O... elems) {
        return Queue.of(elems);
    }

    public static <O> TailCall<O> Call(Supplier<TailCall<O>> tailCall) {
        return TailCall.call(tailCall);
    }

    public static <O> TailCall<O> Yield(O result) {
        return TailCall.yield(result);
    }
}
