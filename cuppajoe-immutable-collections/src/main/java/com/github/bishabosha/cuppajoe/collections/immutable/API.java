package com.github.bishabosha.cuppajoe.collections.immutable;

public final class API {

    private API() {
    }

    @SafeVarargs
    public static <O extends Comparable<O>>
    Tree<O>
    Tree(O... values) {
        return Tree.ofAll(values);
    }

    public static <O>
    List<O>
    List() {
        return List.empty();
    }

    public static <O>
    List<O>
    List(O a) {
        return List.concat(a, List.empty());
    }

    public static <O>
    List<O>
    List(O a, O b) {
        return List.concat(a, List.singleton(b));
    }

    public static <O>
    List<O>
    List(O a, O b, O c) {
        return List.concat(a, List.concat(b, List.singleton(c)));
    }

    public static <O>
    List<O>
    List(O a, O b, O c, O d) {
        return List.concat(a, List.concat(b, List.concat(c, List.singleton(d))));
    }

    public static <O>
    List<O>
    List(O a, O b, O c, O d, O e) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.singleton(e)))));
    }

    public static <O>
    List<O>
    List(O a, O b, O c, O d, O e, O f) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.singleton(f))))));
    }

    public static <O>
    List<O>
    List(O a, O b, O c, O d, O e, O f, O g) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.singleton(g)))))));
    }

    public static <O>
    List<O>
    List(O a, O b, O c, O d, O e, O f, O g, O h) {
        return List.concat(a, List.concat(b, List.concat(c, List.concat(d, List.concat(e, List.concat(f, List.concat(g, List.singleton(h))))))));
    }

    @SafeVarargs
    public static <O>
    List<O>
    List(O... elems) {
        return List.ofAll(elems);
    }

    public static <O>
    Queue<O>
    Queue() {
        return Queue.empty();
    }

    public static <O>
    Queue<O>
    Queue(O elem) {
        return Queue.singleton(elem);
    }

    @SafeVarargs
    public static <O>
    Queue<O>
    Queue(O... elems) {
        return Queue.ofAll(elems);
    }
}
