package io.cuppajoe.tuples;

@FunctionalInterface
public interface Unapply<T extends Tuple> {
    T unapply();
}
