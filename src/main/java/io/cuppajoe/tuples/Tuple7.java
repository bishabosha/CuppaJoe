package io.cuppajoe.tuples;

import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func7;
import io.cuppajoe.typeclass.compose.Compose7;

import java.util.Objects;

public final class Tuple7<A, B, C, D, E, F, G> implements Tuple, Unapply7<A, B, C, D, E, F, G>, Compose7<A, B, C, D, E, F, G> {

    public final A $1;
    public final B $2;
    public final C $3;
    public final D $4;
    public final E $5;
    public final F $6;
    public final G $7;

    Tuple7(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
        this.$7 = $7;
    }

    public Tuple7<A, B, C, D, E, F, G> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 7;
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
            case 7: return $7;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(Func7<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends O> mapper) {
        return mapper.apply($1, $2, $3, $4, $5, $6, $7);
    }

    public <AA, BB, CC, DD, EE, FF, GG> Tuple7<AA, BB, CC, DD, EE, FF, GG>
    flatMap(Func7<A, B, C, D, E, F, G, Tuple7<AA, BB, CC, DD, EE, FF, GG>> mapper) {
        return mapper.apply($1, $2, $3, $4, $5, $6, $7);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3, $4, $5, $6, $7);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple7.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3)
                        && Objects.equals($4, o.$4) && Objects.equals($5, o.$5) && Objects.equals($6, o.$6)
                        && Objects.equals($7, o.$7))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ", " + $4 + ", " + $5 + ", " + $6 + ", " + $7 + ")";
    }
}
