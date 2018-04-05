package io.cuppajoe.tuples;

public enum Unit implements Tuple, Unapply0 {
    INSTANCE;

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public String toString() {
        return "()";
    }
}
