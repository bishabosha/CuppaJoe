package io.cuppajoe.tuples;

import static io.cuppajoe.API.Tuple;

public interface Unapply0 extends Unapply<Product0> {
    default Product0 unapply() {
        return Tuple();
    }
}
