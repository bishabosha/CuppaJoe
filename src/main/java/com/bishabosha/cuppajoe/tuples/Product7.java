package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Some;
import com.bishabosha.cuppajoe.functions.Func7;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product7<A, B, C, D, E, F, G> extends Product, Unapply7<A, B, C, D, E, F, G> {

    A $1();
    B $2();
    C $3();
    D $4();
    E $5();
    F $6();
    G $7();

    default Option<Product7<A, B, C, D, E, F, G>> unapply() {
        return Some.of(this);
    }

    @Override
    default int arity() {
        return 7;
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
            default: throw new IndexOutOfBoundsException();
        }
    }

    default <O> O map(Func7<A, B, C, D, E, F, G, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5(), $6(), $7());
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2, this::$3, this::$4, this::$5, this::$6, this::$7).iterator();
    }
}