package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.control.Some;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Product0 extends Product, Unapply0 {

    default Option<Product0> unapply() {
        return Some.of(this);
    }

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default Object $(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    @NotNull
    default Iterator<Object> iterator() {
        return Iterables.empty().iterator();
    }
}
