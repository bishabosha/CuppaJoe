package com.bishabosha.cuppajoe.tuples;

import com.bishabosha.cuppajoe.control.Option;

import static com.bishabosha.cuppajoe.API.Some;
import static com.bishabosha.cuppajoe.API.Tuple;

public interface Unapply0 {
    default Option<Product0> unapply() {
        return Some(Tuple());
    }
}
