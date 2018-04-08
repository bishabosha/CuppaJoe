package com.github.bishabosha.cuppajoe.higher.value.internal.value1;

import com.github.bishabosha.cuppajoe.higher.value.Value1.Value;

public class Box<E> implements Value<E> {
    private final E value;

    public Box(E value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public E get() {
        return value;
    }
}
