package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.collections.immutable.tuples.*;

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

    public static Unit
    Tuple() {
        return Tuple.of();
    }

    public static <A>
    Tuple1<A>
    Tuple(A $1) {
        return Tuple.of($1);
    }

    public static <A, B>
    Tuple2<A, B>
    Tuple(A $1, B $2) {
        return Tuple.of($1, $2);
    }

    public static <A, B, C>
    Tuple3<A, B, C>
    Tuple(A $1, B $2, C $3) {
        return Tuple.of($1, $2, $3);
    }

    public static <A, B, C, D>
    Tuple4<A, B, C, D>
    Tuple(A $1, B $2, C $3, D $4) {
        return Tuple.of($1, $2, $3, $4);
    }

    public static <A, B, C, D, E>
    Tuple5<A, B, C, D, E>
    Tuple(A $1, B $2, C $3, D $4, E $5) {
        return Tuple.of($1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F>
    Tuple6<A, B, C, D, E, F>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6) {
        return Tuple.of($1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G>
    Tuple7<A, B, C, D, E, F, G>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7) {
        return Tuple.of($1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H>
    Tuple8<A, B, C, D, E, F, G, H>
    Tuple(A $1, B $2, C $3, D $4, E $5, F $6, G $7, H $8) {
        return Tuple.of($1, $2, $3, $4, $5, $6, $7, $8);
    }
}
