package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.functions.Func5;

import java.util.Iterator;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Some;

public interface Product5<A, B, C, D, E> extends Product {

    A $1();
    B $2();
    C $3();
    D $4();
    E $5();

    default Option<Product5<A, B, C, D, E>> unapply() {
        return Some(this);
    }

    @Override
    default int arity() {
        return 5;
    }

    @Override
    default Option<Object> $(int index) {
        switch (index) {
            case 1: return Some($1());
            case 2: return Some($2());
            case 3: return Some($3());
            case 4: return Some($4());
            case 5: return Some($5());
            default: return Nothing();
        }
    }

    default <O> O map(Func5<A, B, C, D, E, O> mapper) {
        return mapper.apply($1(), $2(), $3(), $4(), $5());
    }

    @Override
    default Iterator iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2, this::$3, this::$4, this::$5).iterator();
    }
}
