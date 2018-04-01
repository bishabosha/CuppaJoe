package io.cuppajoe;

import io.cuppajoe.tuples.Unapply0;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Unit implements Comparable<Unit>, Unapply0 {
    public static final Unit INSTANCE = new Unit();

    private Unit() {
    }

    @Contract(pure = true)
    @Override
    public int compareTo(@NotNull Unit o) {
        return 0;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        return obj == INSTANCE;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Unit";
    }
}
