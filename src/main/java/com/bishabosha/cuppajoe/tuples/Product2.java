package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Some;
import com.bishabosha.cuppajoe.functions.Func2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product2<A, B> extends Product, Unapply2<A, B> {

    A $1();
    B $2();

    default Option<Product2<A, B>> unapply() {
        return Some.of(this);
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

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers(this::$1, this::$2).iterator();
    }
}
