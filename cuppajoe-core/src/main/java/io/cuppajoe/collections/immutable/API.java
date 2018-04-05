package io.cuppajoe.collections.immutable;

public final class API {

    private API() {
    }

    public static <O extends Comparable<O>> Tree<O> Tree(O... values) {
        return Tree.of(values);
    }

    public static <O> List<O> List() {
        return List.empty();
    }

    public static <O> List<O> List(O a) {
        return List.concat(a, List.empty());
    }

    public static <O> List<O> List(O a, O b) {
        return List.concat(a, List.of(b));
    }

    public static <O> List<O> List(O a, O b, O c) {
        return List.concat(a, List.concat(b, List.of(c)));
    }

    public static <O> List<O> List(O a, O b, O c, O d) {
        return List.concat(a, List.concat(b, List.concat(c, List.of(d))));
    }

    public static <O> List<O> List(O a, O b, O c, O d, O e) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.of(e)))));
    }

    public static <O> List<O> List(O a, O b, O c, O d, O e, O f) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.of(f))))));
    }

    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.of(g)))))));
    }

    public static <O> List<O> List(O a, O b, O c, O d, O e, O f, O g, O h) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.concat(g, List.of(h))))))));
    }

    public static <O> List<O> List(O... elems) {
        return List.of(elems);
    }

    public static <O> Queue<O> Queue() {
        return Queue.empty();
    }

    public static <O> Queue<O> Queue(O elem) {
        return Queue.of(elem);
    }

    public static <O> Queue<O> Queue(O... elems) {
        return Queue.of(elems);
    }
}
