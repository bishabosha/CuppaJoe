package io.cuppajoe.tuples;

import io.cuppajoe.Iterables;
import io.cuppajoe.functions.Func8;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product8<A, B, C, D, E, F, G, H> extends Product, Unapply8<A, B, C, D, E, F, G, H> {

    A $1();
    B $2();
    C $3();
    D $4();
    E $5();
    F $6();
    G $7();
    H $8();

    default Product8<A, B, C, D, E, F, G, H> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 8;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            case 2: return $2();
            case 3: return $3();
            case 4: return $4();
            case 5: return $5();
            case 6: return $6();
            case 7: return $7();
            case 8: return $8();
            default: throw new IndexOutOfBoundsException();
        }
    }

    default <O> O map(Func8<A, B, C, D, E, F, G, H, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7(), $8());
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2, this::$3, this::$4, this::$5, this::$6, this::$7, this::$8).iterator();
    }
}
