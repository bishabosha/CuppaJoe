package io.cuppajoe.tuples;

public interface Unapply0 extends Unapply<Unit> {
    default Unit unapply() {
        return Unit.INSTANCE;
    }
}
