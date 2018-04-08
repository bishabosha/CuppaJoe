package com.github.bishabosha.cuppajoe.tuples;

import com.github.bishabosha.cuppajoe.higher.functions.*;
import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Tuple {

    int arity();

    Object get(int index);

    default Value<Object> tryGet(int index) {
        return CheckedFunc1.lift(this::get).apply(index);
    }

    default boolean contains(Object o) {
        var count = 1;
        while (count <= arity()) {
            if (Objects.equals(get(count++), o)) {
                return true;
            }
        }
        return false;
    }

    default <R> Iterator<R> iterator() {
        return new TupleIterator<>(this);
    }

    static Unit
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

    static <R>
    Func1<Unit, R>
    tupled(Supplier<? extends R> func) {
        return x -> func.get();
    }

    static <A, R>
    Func1<Tuple1<A>, R>
    tupled(Function<? super A, ? extends R> func) {
        return x -> func.apply(x.$1);
    }

    static <A, B, R>
    Func1<Tuple2<A, B>, R>
    tupled(BiFunction<? super A, ? super B, ? extends R> func) {
        return x -> func.apply(x.$1, x.$2);
    }

    static <A, B, C, R>
    Func1<Tuple3<A, B, C>, R>
    tupled(Func3<A, B, C, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3);
    }

    static <A, B, C, D, R>
    Func1<Tuple4<A, B, C, D>, R>
    tupled(Func4<A, B, C, D, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3, x.$4);
    }

    static <A, B, C, D, E, R>
    Func1<Tuple5<A, B, C, D, E>, R>
    tupled(Func5<A, B, C, D, E, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3, x.$4, x.$5);
    }

    static <A, B, C, D, E, F, R>
    Func1<Tuple6<A, B, C, D, E, F>, R>
    tupled(Func6<A, B, C, D, E, F, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6);
    }

    static <A, B, C, D, E, F, G, R>
    Func1<Tuple7<A, B, C, D, E, F, G>, R>
    tupled(Func7<A, B, C, D, E, F, G, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7);
    }

    static <A, B, C, D, E, F, G, H, R>
    Func1<Tuple8<A, B, C, D, E, F, G, H>, R>
    tupled(Func8<A, B, C, D, E, F, G, H, R> func) {
        return x -> func.apply(x.$1, x.$2, x.$3, x.$4, x.$5, x.$6, x.$7, x.$8);
    }
}
