package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.*;

import static com.github.bishabosha.cuppajoe.match.API.PatternFor;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> Node$(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFor(Tree.Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> Leaf$() {
        return PatternFor(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> Cons$(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFor(List.Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> Nil$() {
        return PatternFor(List.Nil.INSTANCE);
    }

    public static final Pattern<Unit> Unit$ = PatternFor(Unit.INSTANCE);

    public static <A> Pattern<Tuple1<A>> Tuple1$(Pattern<A> $1) {
        return PatternFor(Tuple1.class, $1);
    }

    public static <A, B> Pattern<Tuple2<A, B>> Tuple2$(Pattern<A> $1, Pattern<B> $2) {
        return PatternFor(Tuple2.class, $1, $2);
    }

    public static <A, B, C> Pattern<Tuple3<A, B, C>> Tuple3$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return PatternFor(Tuple3.class, $1, $2, $3);
    }

    public static <A, B, C, D> Pattern<Tuple4<A, B, C, D>> Tuple4$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return PatternFor(Tuple4.class, $1, $2, $3, $4);
    }

    public static <A, B, C, D, E> Pattern<Tuple5<A, B, C, D, E>> Tuple5$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return PatternFor(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F> Pattern<Tuple6<A, B, C, D, E, F>> Tuple6$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return PatternFor(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G> Pattern<Tuple7<A, B, C, D, E, F, G>> Tuple7$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return PatternFor(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H> Pattern<Tuple8<A, B, C, D, E, F, G, H>> Tuple8$(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return PatternFor(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }
}
