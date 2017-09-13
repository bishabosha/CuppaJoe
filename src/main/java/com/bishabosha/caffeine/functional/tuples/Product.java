package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Library;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.functions.Func1;

import java.util.List;

public interface Product extends Iterable {

    int arity();

    Object $(int index);

    default Option<Object> $lifted(int index) {
        return Func1.of(this::$).lifted().apply(index);
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
