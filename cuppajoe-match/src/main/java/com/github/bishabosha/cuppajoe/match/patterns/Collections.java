package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.*;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Either.Left;
import com.github.bishabosha.cuppajoe.control.Either.Right;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;

import static com.github.bishabosha.cuppajoe.match.API.PatternFor;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> node(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFor(Tree.Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> leaf() {
        return PatternFor(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> cons(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFor(List.Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> nil() {
        return PatternFor(List.Nil.INSTANCE);
    }

    public static final Pattern<Unit> unit = PatternFor(Unit.INSTANCE);

    public static <A> Pattern<Tuple1<A>> tuple(Pattern<A> $1) {
        return PatternFor(Tuple1.class, $1);
    }

    public static <A, B> Pattern<Tuple2<A, B>> tuple(Pattern<A> $1, Pattern<B> $2) {
        return PatternFor(Tuple2.class, $1, $2);
    }

    public static <A, B, C> Pattern<Tuple3<A, B, C>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return PatternFor(Tuple3.class, $1, $2, $3);
    }

    public static <A, B, C, D> Pattern<Tuple4<A, B, C, D>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return PatternFor(Tuple4.class, $1, $2, $3, $4);
    }

    public static <A, B, C, D, E> Pattern<Tuple5<A, B, C, D, E>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return PatternFor(Tuple5.class, $1, $2, $3, $4, $5);
    }

    public static <A, B, C, D, E, F> Pattern<Tuple6<A, B, C, D, E, F>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return PatternFor(Tuple6.class, $1, $2, $3, $4, $5, $6);
    }

    public static <A, B, C, D, E, F, G> Pattern<Tuple7<A, B, C, D, E, F, G>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return PatternFor(Tuple7.class, $1, $2, $3, $4, $5, $6, $7);
    }

    public static <A, B, C, D, E, F, G, H> Pattern<Tuple8<A, B, C, D, E, F, G, H>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return PatternFor(Tuple8.class, $1, $2, $3, $4, $5, $6, $7, $8);
    }

    public static <O> Pattern<Try<O>> success(Pattern<O> value) {
        return PatternFor(Try.Success.class, value);
    }

    public static <O> Pattern<Try<O>> failure(Pattern<Exception> error) {
        return PatternFor(Try.Failure.class, error);
    }

    public static <L, R> Pattern<Either<L, R>> left(Pattern<L> value) {
        return PatternFor(Left.class, value);
    }

    public static <L, R> Pattern<Either<L, R>> right(Pattern<R> value) {
        return PatternFor(Right.class, value);
    }

    public static <O> Pattern<Lazy<O>> lazy(Pattern<O> value) {
        return PatternFor(Lazy.class, value);
    }

    public static <O> Pattern<Option<O>> some(Pattern<O> value) {
        return PatternFor(Option.Some.class, value);
    }

    public static <O> Pattern<Option<O>> none() {
        return PatternFor(Option.None.INSTANCE);
    }
}
