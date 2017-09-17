package com.bishabosha.cuppajoe.tuples;

public interface Apply3<A, B, C, R> {
    R apply(Product3<A, B, C> tuple);
}
