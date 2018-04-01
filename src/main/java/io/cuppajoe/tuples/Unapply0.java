package io.cuppajoe.tuples;

public interface Unapply0 extends Unapply<Product0> {
    default Product0 unapply() {
        return Tuple0.INSTANCE;
    }
}
