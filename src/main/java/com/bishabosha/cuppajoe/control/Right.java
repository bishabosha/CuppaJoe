package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Right<L, R> implements Either<L, R> {

    private final R value;

    @NotNull
    @Contract(pure = true)
    public static final Pattern $Right(Pattern pattern) {
        return x -> x instanceof Right ? pattern.test(((Right) x).get()) : Pattern.FAIL;
    }

    private Right(R value) {
        this.value = value;
    }

    @NotNull
    public static <L, R> Either<L, R> of(R right) {
        return new Right<>(right);
    }

    @Override
    public R get() {
        return value;
    }

    @Override
    public L getLeft() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Right)) {
            return false;
        }
        return Objects.equals(get(), ((Right) obj).get());
    }

    @Override
    public String toString() {
        return "Right("+get()+")";
    }

    @Override
    public Iterator<R> iterator() {
        return Iterables.singletonIt(this::get);
    }
}
