package com.github.bishabosha.cuppajoe.util;

import com.github.bishabosha.cuppajoe.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <O, T extends Collection<O>> T fill(@NonNull T collection, @NonNull O[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        Collections.addAll(collection, values);
        return collection;
    }

    public static <O extends Collection<Integer>> O fill(@NonNull O collection, @NonNull int[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Double>> O fill(@NonNull O collection, @NonNull double[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Float>> O fill(@NonNull O collection, @NonNull float[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Long>> O fill(@NonNull O collection, @NonNull long[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Short>> O fill(@NonNull O collection, @NonNull short[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Byte>> O fill(@NonNull O collection, @NonNull byte[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Boolean>> O fill(@NonNull O collection, @NonNull boolean[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Character>> O fill(@NonNull O collection, @NonNull char[] values) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(values, "values");
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }
}
