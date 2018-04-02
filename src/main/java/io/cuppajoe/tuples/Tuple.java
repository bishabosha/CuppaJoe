package io.cuppajoe.tuples;

import io.cuppajoe.Library;
import io.cuppajoe.control.Try;
import io.cuppajoe.functions.Func2;

public interface Tuple extends Iterable<Object> {

    int arity();

    Object $(int index);

    default Try<Object> lift$(int index) {
        return Try.of(() -> $(index));
    }

    default <A> A flatten(A accumulator, Func2<A, Object, A> mapper) {
        return Library.foldLeft(Tuple.class, this, accumulator, mapper);
    }

    default boolean contains(Object o) {
        for (var elem: this) {
            if (elem.equals(o)) {
                return true;
            }
        }
        return false;
    }

    static
    Unit
    of() {
        return Unit.INSTANCE;
    }

    static <A>
    Tuple1<A>
    of(A $1) {
        return new Tuple1<>($1);
    }

    static <A, B>
    Tuple2<A, B>
    of(A $1, B $2) {
        return new Tuple2<>($1, $2);
    }

    static <A, B, C>
    Tuple3<A, B, C>
    of(A $1, B $2, C $3) {
        return new Tuple3<>($1, $2, $3);
    }

    static <A, B, C, D>
    Tuple4<A, B, C, D>
    of(A $1, B $2, C $3, D $4) {
        return new Tuple4<>($1, $2, $3, $4);
    }

    static <A, B, C, D, E>
    Tuple5<A, B, C, D, E>
    of(A $1, B $2, C $3, D $4, E $5) {
        return new Tuple5<>($1, $2, $3, $4, $5);
    }

    static <A, B, C, D, E, F>
    Tuple6<A, B, C, D, E, F>
    of(A $1, B $2, C $3, D $4, E $5, F $6) {
        return new Tuple6<>($1, $2, $3, $4, $5, $6);
    }

    static <A, B, C, D, E, F, G>
    Tuple7<A, B, C, D, E, F, G>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return new Tuple7<>($1, $2, $3, $4, $5, $6, $7);
    }

    static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    of(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return new Tuple8<>($1, $2, $3, $4, $5, $6, $7, $8);
    }
}
