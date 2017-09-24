package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;

public interface Unapply2<A, B> {
    Option<Product2<A, B>> unapply();
}
