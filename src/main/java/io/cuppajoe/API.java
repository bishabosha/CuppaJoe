package io.cuppajoe;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.List.Empty;
import io.cuppajoe.collections.immutable.Queue;
import io.cuppajoe.control.Either;
import io.cuppajoe.control.Either.Left;
import io.cuppajoe.control.Either.Right;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Nothing;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;
import io.cuppajoe.functions.CheckedFunc0;
import io.cuppajoe.functions.Func0;
import io.cuppajoe.patterns.Case;
import io.cuppajoe.patterns.Matcher;
import io.cuppajoe.tuples.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public final class API {

    public static
    Product0
    Tuple() {
        return Tuple0.getInstance();
    }

    public static <A>
    Product1<A>
    Tuple(A $1) {
        return Tuple1.of($1);
    }

    public static <A, B>
    Product2<A, B>
    Tuple(A $1, B $2) {
        return Tuple2.of($1, $2);
    }

    public static <A, B, C>
    Product3<A, B, C> Tuple(A $1, B $2, C $3) {
        return Tuple3.of($1, $2, $3);
    }

    public static <A, B, C, D>
    Product4<A, B, C, D>
    Tuple(A $1, B $2, C $3, D $4) {
        return Tuple4.of($1, $2, $3, $4);
    }

    public static <A, B, C, D, E>
    Product5<A, B, C, D, E>
    Tuple(A $1, B $2, C $3, D $4, E $5) {
        return Tuple5.of($1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F>
    Product6<A, B, C, D, E, F>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6) {
        return Tuple6.of($1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G>
    Product7<A, B, C, D, E, F, G>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return Tuple7.of($1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H>
    Product8<A, B, C, D, E, F, G, H>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return Tuple8.of($1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static <L, R> Left<L, R> Left(L left) {
        return Either.left(left);
    }

    public static <L, R> Right<L, R> Right(R right) {
        return Either.right(right);
    }

    public static <O> Option<O> Option(BooleanSupplier condition, Func0<O> elem) {
        return condition.getAsBoolean() ? Option.of(elem.get()) : Nothing();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Some<O> Some(@Nullable O elem) {
        return Option.some(elem);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Nothing<O> Nothing() {
        return Option.nothing();
    }

    @NotNull
    public static <O> Try<O> Try(CheckedFunc0<O> getter) {
        return Try.of(getter);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Success<O> Success(@Nullable O value) {
        return Try.success(value);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Failure<O> Failure(Exception error) {
        return Try.failure(error);
    }

    @NotNull
    public static <I> Matcher<I> Match(I toMatch) {
        return Matcher.create(toMatch);
    }

    public static <I, O> Option<O> Match(Option<I> toMatch, Case<I, O> options) {
        return toMatch.isEmpty() ? Nothing() : options.match(toMatch.get());
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Empty<O> List() {
        return List.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a) {
        return List.concat(a, List.empty());
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b) {
        return List.concat(a, List.of(b));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c) {
        return List.concat(a, List.concat(b, List.of(c)));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c, O d) {
        return List.concat(a, List.concat(b, List.concat(c, List.of(d))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c, O d, O e) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.of(e)))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c, O d, O e, O f) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.of(f))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c, O d, O e, O f, O g) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.of(g)))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Cons<O> List(O a, O b, O c, O d, O e, O f, O g, O h) {
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
}
