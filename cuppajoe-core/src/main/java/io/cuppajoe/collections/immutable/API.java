package io.cuppajoe.collections.immutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class API {

    @NotNull
    @Contract(pure = true)
    public static <O extends Comparable<O>> Tree<O> Tree(O... values) {
        return Tree.of(values);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List() {
        return List.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a) {
        return List.concat(a, List.empty());
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b) {
        return List.concat(a, List.of(b));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c) {
        return List.concat(a, List.concat(b, List.of(c)));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d) {
        return List.concat(a, List.concat(b, List.concat(c, List.of(d))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.of(e)))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.of(f))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.of(g)))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g, O h) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.concat(g, List.of(h))))))));
    }

    @NotNull
    @Contract(pure = true)
    public static <O> List<O> List(O... elems) {
        return List.of(elems);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue() {
        return Queue.empty();
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue(O elem) {
        return Queue.of(elem);
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Queue<O> Queue(O... elems) {
        return Queue.of(elems);
    }
}
