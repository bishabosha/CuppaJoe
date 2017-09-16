package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.functions.Func1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Supplier;

import static com.bishabosha.caffeine.functional.API.*;

public interface Product1<A> extends Product, Unapply1<A> {

    A $1();

    default Product1<A> unapply() {
        return this;
    }

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default Object $(int index) {
        switch (index) {
            case 1: return $1();
            default: throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    default Iterator<Object> iterator() {
        return Iterables.ofSuppliers((Supplier<Object>) this::$1).iterator();
    }

    default <O> O map(Func1<A, O> mapper) {
        return mapper.apply($1());
    }
}
