package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.control.Option;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static com.bishabosha.caffeine.functional.API.Some;

public interface Product0 extends Product, Unapply0 {

    default Product0 unapply() {
        return this;
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
