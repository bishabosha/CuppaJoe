package io.cuppajoe.match.patterns;

import io.cuppajoe.Lazy;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.immutable.Tree.Node;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;
import io.cuppajoe.match.PatternFactory;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> $Node(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFactory.unapply3(Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> $Leaf() {
        return PatternFactory.unapply0(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> $Cons(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFactory.unapply2(Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> $Nil() {
        return PatternFactory.unapply0(List.Nil.INSTANCE);
    }

    public static <O> Pattern<Try<O>> $Success(Pattern<O> $value) {
        return PatternFactory.unapply1(Success.class, $value);
    }

    public static <O> Pattern<Try<O>> $Failure(Pattern<Exception> $error) {
        return PatternFactory.unapply1(Failure.class, $error);
    }

    public static <O> Pattern<Lazy<O>> $Lazy(Pattern<O> $value) {
        return PatternFactory.unapply1(Lazy.class, $value);
    }

    public static <O> Pattern<Option<O>> $Some(Pattern<O> $value) {
        return PatternFactory.unapply1(Some.class, $value);
    }

    public static <O> Pattern<Option<O>> $None() {
        return PatternFactory.unapply0(Option.None.INSTANCE);
    }
}
