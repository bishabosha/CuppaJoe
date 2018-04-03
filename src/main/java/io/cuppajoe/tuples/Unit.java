package io.cuppajoe.tuples;

import io.cuppajoe.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Unit implements Tuple, Unapply0, Iterable<Unit> {
    INSTANCE;

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    public Iterator<Unit> iterator() {
        return Iterators.empty();
    }

    @Override
    public String toString() {
        return "()";
    }
}
