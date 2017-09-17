package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.functions.Func3;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product3<A, B, C> extends Product, Unapply3<A, B, C> {

    A $1();
    B $2();
    C $3();

    default Product3<A, B, C> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 3;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            case 2: return $2();
            case 3: return $3();
            default: throw new IndexOutOfBoundsException();
        }
    }

    default <O> O map(Func3<A, B, C, O> mapper) {
        return mapper.apply($1(), $2(), $3());
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2, this::$3).iterator();
    }
}
