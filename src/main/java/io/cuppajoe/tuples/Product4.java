package io.cuppajoe.tuples;

import io.cuppajoe.Iterables;
import io.cuppajoe.functions.Func4;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product4<A, B, C, D> extends Product, Unapply4<A, B, C, D> {

    A $1();
    B $2();
    C $3();
    D $4();

    default Product4<A, B, C, D> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 4;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            case 2: return $2();
            case 3: return $3();
            case 4: return $4();
            default: throw new IndexOutOfBoundsException();
        }
    }

    default <O> O map(Func4<A, B, C, D, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4());
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2, this::$3, this::$4).iterator();
    }
}
