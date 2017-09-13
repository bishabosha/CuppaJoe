package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.functions.Func1;

import java.util.Iterator;

import static com.bishabosha.caffeine.functional.API.*;

public interface Product1<A> extends Product {

    A $1();

    default Option<Product1<A>> unapply() {
        return Some(this);
    }

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default Option<Object> $(int index) {
        switch (index) {
            case 1: return Some($1());
            default: return Nothing();
        }
    }

    @Override
    default Iterator iterator() {
        return Iterables.ofSuppliers(this::$1).iterator();
    }

    default <O> O map(Func1<A, O> mapper) {
        return mapper.apply($1());
    }
}
