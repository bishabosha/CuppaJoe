package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.Array;
import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.*;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Lazy;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.control.Try;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern.*;

import java.util.Objects;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.List;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.collections.immutable.Tree.Leaf;
import static com.github.bishabosha.cuppajoe.match.patterns.Pattern.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.is;

public class Collections {

    @SuppressWarnings("unchecked")
    public static <O extends Comparable<O>> Branch<Tree<O>> node(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return branchN(
            Tree.Node.class,
            Tuple($node, x -> x.node),
            Tuple($left, x -> x.left),
            Tuple($right, x -> x.right)
        );
    }

    public static <O extends Comparable<O>> Empty<Tree<O>> leaf() {
        return is(Leaf());
    }

    @SuppressWarnings("unchecked")
    public static <O> Branch<List<O>> cons(Pattern<O> $x, Pattern<List<O>> $xs) {
        return branchN(
            List.Cons.class,
            Tuple($x, x -> x.head),
            Tuple($xs, x -> x.tail)
        );
    }

    public static <O> Empty<List<O>> nil() {
        return is(List());
    }

    @SuppressWarnings("unchecked")
    public static <T> Branch<Option<T>> some(Pattern<T> value) {
        return branch1(Option.Some.class, value, x -> x.value);
    }

    public static <T> Empty<Option<T>> none() {
        return is(None());
    }

    @SuppressWarnings("unchecked")
    public static <O> Branch<Try<O>> success(Pattern<O> value) {
        return branch1(Try.Success.class, value, x -> x.value);
    }

    @SuppressWarnings("unchecked")
    public static <O> Branch<Try<O>> failure(Pattern<Exception> error) {
        return branch1(Try.Failure.class, error, x -> x.error);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Branch<Either<L, R>> left(Pattern<L> value) {
        return branch1(Either.Left.class, value, x -> x.value);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> Branch<Either<L, R>> right(Pattern<R> value) {
        return branch1(Either.Right.class, value, x -> x.value);
    }

    public static <O> Branch<Lazy<O>> lazy(Pattern<O> value) {
        return branch1(Objects::nonNull, value, Lazy::memo);
    }

    public static Empty<Unit> unit() {
        return is(Tuple());
    }

    @SafeVarargs
    public static <O> Branch<O[]> arr(Pattern<O>... values) {
        return branchGenerator(
            arr -> arr != null && arr.length >= values.length,
            i -> arr -> arr[i],
            values
        );
    }

    @SafeVarargs
    public static <O> Branch<Array<O>> array(Pattern<O>... values) {
        return branchGenerator(
            arr -> arr != null && arr.size() >= values.length,
            i -> arr -> arr.get(i),
            values
        );
    }

    public static <A> Branch<Tuple1<A>> tuple(Pattern<A> $1) {
        return branch1(Objects::nonNull, $1, t -> t.$1);
    }

    public static <A, B> Branch<Tuple2<A, B>> tuple(Pattern<A> $1, Pattern<B> $2) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2)
        );
    }

    public static <A, B, C> Branch<Tuple3<A, B, C>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3)
        );
    }

    public static <A, B, C, D> Branch<Tuple4<A, B, C, D>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3),
            Tuple($4, t -> t.$4)
        );
    }

    public static <A, B, C, D, E> Branch<Tuple5<A, B, C, D, E>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3),
            Tuple($4, t -> t.$4),
            Tuple($5, t -> t.$5)
        );
    }

    public static <A, B, C, D, E, F> Branch<Tuple6<A, B, C, D, E, F>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3),
            Tuple($4, t -> t.$4),
            Tuple($5, t -> t.$5),
            Tuple($6, t -> t.$6)
        );
    }

    public static <A, B, C, D, E, F, G> Branch<Tuple7<A, B, C, D, E, F, G>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3),
            Tuple($4, t -> t.$4),
            Tuple($5, t -> t.$5),
            Tuple($6, t -> t.$6),
            Tuple($7, t -> t.$7)
        );
    }

    public static <A, B, C, D, E, F, G, H> Branch<Tuple8<A, B, C, D, E, F, G, H>> tuple(Pattern<A> $1, Pattern<B> $2, Pattern<C> $3, Pattern<D> $4, Pattern<E> $5, Pattern<F> $6, Pattern<G> $7, Pattern<H> $8) {
        return branchN(
            Objects::nonNull,
            Tuple($1, t -> t.$1),
            Tuple($2, t -> t.$2),
            Tuple($3, t -> t.$3),
            Tuple($4, t -> t.$4),
            Tuple($5, t -> t.$5),
            Tuple($6, t -> t.$6),
            Tuple($7, t -> t.$7),
            Tuple($8, t -> t.$8)
        );
    }
}
