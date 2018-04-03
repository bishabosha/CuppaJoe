package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func6;
import io.cuppajoe.typeclass.compose.Compose6;

import java.util.Objects;

public final class Tuple6<A, B, C, D, E, F> implements Tuple, Unapply6<A, B, C, D, E, F>, Compose6<A, B, C, D, E, F> {

    public final A $1;
    public final B $2;
    public final C $3;
    public final D $4;
    public final E $5;
    public final F $6;

    Tuple6(A $1, B $2, C $3, D $4, E $5, F $6) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
    }

    public Tuple6<A, B, C, D, E, F> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 6;
    }

    @Override
    public Object get(int index) {
        switch (index) {
            case 1: return $1;
            case 2: return $2;
            case 3: return $3;
            case 4: return $4;
            case 5: return $5;
            case 6: return $6;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(Func6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends O> mapper) {
        return mapper.apply($1, $2, $3, $4, $5, $6);
    }

    public <AA, BB, CC, DD, EE, FF> Tuple6<AA, BB, CC, DD, EE, FF> flatMap(Func6<A, B, C, D, E, F, Tuple6<AA, BB, CC, DD, EE, FF>> mapper) {
        return mapper.apply($1, $2, $3, $4, $5, $6);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3, $4, $5, $6);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple6.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3)
                        && Objects.equals($4, o.$4) && Objects.equals($5, o.$5) && Objects.equals($6, o.$6))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ", " + $4 + ", " + $5 + ", " + $6 + ")";
    }
}
