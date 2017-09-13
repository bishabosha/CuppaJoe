package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Library;
import com.bishabosha.caffeine.functional.Option;

import java.util.List;

public interface Product extends Iterable {

    int arity();

    Option<Object> $(int index);

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
