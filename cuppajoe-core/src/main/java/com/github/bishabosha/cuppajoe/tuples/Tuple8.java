package com.github.bishabosha.cuppajoe.tuples;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.functions.Func8;
import com.github.bishabosha.cuppajoe.typeclass.compose.Compose8;

import java.util.Objects;

public final class Tuple8<A, B, C, D, E, F, G, H> implements Tuple, Unapply8<A, B, C, D, E, F, G, H>, Compose8<A, B, C, D, E, F, G, H> {

    public final A $1;
    public final B $2;
    public final C $3;
    public final D $4;
    public final E $5;
    public final F $6;
    public final G $7;
    public final H $8;

    Tuple8(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
        this.$6 = $6;
        this.$7 = $7;
        this.$8 = $8;
    }

    public Tuple8<A, B, C, D, E, F, G, H> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 8;
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
            case 5:
                return $5;
            case 6:
                return $6;
            case 7:
                return $7;
            case 8:
                return $8;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(@NonNull Func8<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1, $2, $3, $4, $5, $6, $7, $8);
    }

    public <AA, BB, CC, DD, EE, FF, GG, HH> Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>
    flatMap(@NonNull Func8<A, B, C, D, E, F, G, H, Tuple8<AA, BB, CC, DD, EE, FF, GG, HH>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1, $2, $3, $4, $5, $6, $7, $8);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3, $4, $5, $6, $7, $8);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple8.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3)
                        && Objects.equals($4, o.$4) && Objects.equals($5, o.$5) && Objects.equals($6, o.$6)
                        && Objects.equals($7, o.$7) && Objects.equals($8, o.$8))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ", " + $4 + ", " + $5 + ", " + $6 + ", " + $7 + ", " + $8 + ")";
    }
}
