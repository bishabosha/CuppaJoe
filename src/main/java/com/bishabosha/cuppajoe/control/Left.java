package com.bishabosha.cuppajoe.control;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.patterns.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Left<L, R> implements Either<L, R> {

    private final L value;

    @NotNull
    @Contract(pure = true)
    public static final Pattern $Left(Pattern pattern) {
        return x -> x instanceof Left ? pattern.test(((Left) x).getLeft()) : Pattern.FAIL;
    }

    private Left(L value) {
        this.value = value;
    }

    @NotNull
    public static <L, R> Left<L, R> of(L left) {
        return new Left<>(left);
    }

    public L value() {
        return value;
    }

    @Override
    public R get() {
        throw new NoSuchElementException();
    }

    @Override
    public L getLeft() {
        return value;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLeft());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Left)) {
            return false;
        }
        return Objects.equals(getLeft(), ((Left) obj).getLeft());
    }

    @Override
    public String toString() {
        return "Left("+getLeft()+")";
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return Iterables.<R>empty().iterator();
    }
}
