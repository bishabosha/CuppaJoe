package io.cuppajoe.tuples;

import io.cuppajoe.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Unit implements Tuple, Unapply0 {
    INSTANCE;

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object $(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    @NotNull
    public Iterator<Object> iterator() {
        return Iterators.empty();
    }

    @Override
    public String toString() {
        return "()";
    }
}
