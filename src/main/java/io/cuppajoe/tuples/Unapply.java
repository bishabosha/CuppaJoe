package io.cuppajoe.tuples;

@FunctionalInterface
public interface Unapply<P extends Product> {
    P unapply();
}
