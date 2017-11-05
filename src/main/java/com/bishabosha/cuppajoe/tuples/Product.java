package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Library;
import com.bishabosha.cuppajoe.control.Try;
import com.bishabosha.cuppajoe.functions.Func2;

import java.util.function.Supplier;

import static com.bishabosha.cuppajoe.API.Try;

public interface Product extends Iterable<Object> {

    int arity();

    Object $(int index);

    default Try<Object> try$(int index) {
        return Try(() -> $(index));
    }

    default <A> A flatten(Supplier<A> accumulator, Func2<A, Object, A> mapper) {
        return Library.foldLeft(Product.class, this, accumulator, mapper);
    }

    default boolean contains(Object o) {
        for (Object elem: this) {
            if (elem.equals(o)) {
                return true;
            }
        }
        return false;
    }
}
