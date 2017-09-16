package com.bishabosha.caffeine.functional.tuples;

public interface Apply2<A, B, R> {
    R apply(Product2<A, B> tuple);
}
