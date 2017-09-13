package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.functions.Func2;

import java.util.Iterator;

import static com.bishabosha.caffeine.functional.API.Nothing;
import static com.bishabosha.caffeine.functional.API.Some;

public interface Product2<A, B> extends Product {

    A $1();
    B $2();

    default Option<Product2<A, B>> unapply() {
        return Some(this);
    }

    @Override
    default int arity() {
        return 2;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            case 2: return $2();
            default: throw new IndexOutOfBoundsException();
        }
    }

    default <O> O map(Func2<A, B, O> mapper) {
        return mapper.apply($1(), $2());
    }

    @Override
    default Iterator iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2).iterator();
    }
}
