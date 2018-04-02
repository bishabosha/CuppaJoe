package io.cuppajoe.tuples;

import io.cuppajoe.Iterators;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func5;
import io.cuppajoe.typeclass.compose.Compose5;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public final class Tuple5<A, B, C, D, E> implements Tuple, Unapply5<A, B, C, D, E>, Compose5<A, B, C, D, E> {

    public final A $1;
    public final B $2;
    public final C $3;
    public final D $4;
    public final E $5;

    Tuple5(A $1, B $2, C $3, D $4, E $5) {
        this.$1 = $1;
        this.$2 = $2;
        this.$3 = $3;
        this.$4 = $4;
        this.$5 = $5;
    }

    public Tuple5<A, B, C, D, E> unapply() {
        return this;
    }

    @Override
    public int arity() {
        return 5;
    }

    @Override
    public Object $(int index) {
        switch (index) {
            case 1: return $1;
            case 2: return $2;
            case 3: return $3;
            case 4: return $4;
            case 5: return $5;
            default: throw new IndexOutOfBoundsException();
        }
    }

    
    public <O> O compose(Func5<? super A, ? super B, ? super C, ? super D, ? super E, ? extends O> mapper) {
        return mapper.apply($1, $2, $3, $4, $5);
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return Iterators.of($1, $2, $3, $4, $5);
    }

    public <AA, BB, CC, DD, EE> Tuple5<AA, BB, CC, DD, EE> flatMap(Func5<A, B, C, D, E, Tuple5<AA, BB, CC, DD, EE>> mapper) {
        return mapper.apply($1, $2, $3, $4, $5);
    }

    @Override
    public int hashCode() {
        return Objects.hash($1, $2, $3, $4, $5);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || Option.of(obj)
                .cast(Tuple5.class)
                .map(o -> Objects.equals($1, o.$1) && Objects.equals($2, o.$2) && Objects.equals($3, o.$3)
                        && Objects.equals($4, o.$4) && Objects.equals($5, o.$5))
                .orElse(false);
    }

    @Override
    public String toString() {
        return "(" + $1 + ", " + $2 + ", " + $3 + ", " + $4 + ", " + $5 + ")";
    }
}
