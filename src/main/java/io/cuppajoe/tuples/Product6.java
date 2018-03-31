package io.cuppajoe.tuples;

import io.cuppajoe.Iterators;
import io.cuppajoe.functions.Func6;
import io.cuppajoe.typeclass.compose.Compose6;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product6<A, B, C, D, E, F> extends Product, Unapply6<A, B, C, D, E, F>, Compose6<A, B, C, D, E, F> {

    A $1();
    B $2();
    C $3();
    D $4();
    E $5();
    F $6();

    default Product6<A, B, C, D, E, F> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 6;
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
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    default <O> O compose(Func6<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6());
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterators.ofSuppliers(this::$1, this::$2, this::$3, this::$4, this::$5, this::$6);
    }
}
