package com.github.bishabosha.cuppajoe.collections.immutable.tuples;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.compose.Compose3;
import com.github.bishabosha.cuppajoe.higher.functions.Func3;
import com.github.bishabosha.cuppajoe.higher.unapply.Unapply3;

import java.util.Objects;

public final class Tuple3<A, B, C> implements Tuple, Unapply3<A, B, C>, Compose3<A, B, C> {

    public final A $1;
    public final B $2;
    public final C $3;

    Tuple3(A $1, B $2, C $3) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
    }

    public Tuple3<A, B, C> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 3;
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
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public <O> O compose(@NonNull Func3<? super A, ? super B, ? super C, ? extends O> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1, $2, $3);
    }

    public <AA, BB, CC> Tuple3<AA, BB, CC> flatMap(@NonNull Func3<A, B, C, Tuple3<AA, BB, CC>> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return mapper.apply($1, $2, $3);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple3.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ")";
    }
}
