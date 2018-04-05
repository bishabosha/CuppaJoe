package io.cuppajoe.util;

import java.util.Collection;
import java.util.Collections;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static <O, T extends Collection<O>> T fill(T collection, O[] values) {
        Collections.addAll(collection, values);
        return collection;
    }

    public static <O extends Collection<Integer>> O fill(O collection, int[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Double>> O fill(O collection, double[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Float>> O fill(O collection, float[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Long>> O fill(O collection, long[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Short>> O fill(O collection, short[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Byte>> O fill(O collection, byte[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Boolean>> O fill(O collection, boolean[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }

    public static <O extends Collection<Character>> O fill(O collection, char[] values) {
        for (var i : values) {
            collection.add(i);
        }
        return collection;
    }
}
