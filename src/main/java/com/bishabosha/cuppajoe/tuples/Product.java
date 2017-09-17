package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.Library;
import com.bishabosha.cuppajoe.control.Try;

import java.util.List;

import static com.bishabosha.cuppajoe.API.Try;

public interface Product extends Iterable<Object> {

    int arity();

    Object $(int index);

    default Try<Object> try$(int index) {
        return Try(() -> $(index));
    }

    default List<Object> flatten() {
        return Library.flatten(Product.class, this);
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
