package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func4;
import io.cuppajoe.typeclass.compose.Compose4;

import java.util.Objects;

public final class Tuple4<A, B, C, D> implements Tuple, Unapply4<A, B, C, D>, Compose4<A, B, C, D> {

    public final A $1;
    public final B $2;
    public final C $3;
    public final D $4;

    Tuple4(A $1, B $2, C $3, D $4) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
    }

    public Tuple4<A, B, C, D> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 1:
                return $1;
            case 2:
                return $2;
            case 3:
                return $3;
            case 4:
                return $4;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(Func4<? super A, ? super B, ? super C, ? super D, ? extends O> mapper) {
        return mapper.apply($1, $2, $3, $4);
    }

    public <AA, BB, CC, DD> Tuple4<AA, BB, CC, DD> flatMap(Func4<A, B, C, D, Tuple4<AA, BB, CC, DD>> mapper) {
        return mapper.apply($1, $2, $3, $4);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3, $4);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple4.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3)
                        && Objects.equals($4, o.$4))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ", " + $4 + ")";
    }
}
