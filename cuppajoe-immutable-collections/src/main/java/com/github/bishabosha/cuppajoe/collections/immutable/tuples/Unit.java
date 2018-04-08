package com.github.bishabosha.cuppajoe.collections.immutable.tuples;

import com.github.bishabosha.cuppajoe.higher.unapply.Unapply0;

public enum Unit implements Tuple, Unapply0 {
    INSTANCE;

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public String toString() {
        return "()";
    }
}
