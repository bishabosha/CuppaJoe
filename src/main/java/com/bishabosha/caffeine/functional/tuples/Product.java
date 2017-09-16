package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Library;
import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.functions.Func1;

import java.util.List;

public interface Product extends Iterable<Object> {

    int arity();

    Object $(int index);

    default Option<Object> _$(int index) {
        return Func1.lift(this::$).apply(index);
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
