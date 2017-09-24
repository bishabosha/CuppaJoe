package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;

public interface Unapply3<A, B, C> {
    Option<Product3<A, B, C>> unapply();
}
