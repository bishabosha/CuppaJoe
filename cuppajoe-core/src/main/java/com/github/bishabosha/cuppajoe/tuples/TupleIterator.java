package com.github.bishabosha.cuppajoe.tuples;

import java.util.Iterator;

class TupleIterator<R> implements Iterator<R> {

    private int i = 1;
    private final int arity;
    private final Tuple tuple;

    TupleIterator(Tuple tuple) {
        this.tuple = tuple;
        arity = tuple.arity();
    }

    @Override
    public boolean hasNext() {
        return i <= arity;
    }

    @Override
    public R next() {
        return (R) tuple.get(i++);
    }
}
