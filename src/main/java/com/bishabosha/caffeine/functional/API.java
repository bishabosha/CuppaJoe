package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.tuples.*;

public class API {

    public static Tuple
    Tuple() {
        return Tuple.of();
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
        return Either.left(left);
    }

    public static <L, R> Either<L, R> Right(R right) {
        return Either.right(right);
    }
}
