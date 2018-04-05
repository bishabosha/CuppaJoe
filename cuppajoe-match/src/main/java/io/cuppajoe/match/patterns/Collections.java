package io.cuppajoe.match.patterns;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.immutable.Tree.Node;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> Node$(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFactory.unapply3(Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> Leaf$() {
        return PatternFactory.unapply0(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> Cons$(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFactory.unapply2(Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> Nil$() {
        return PatternFactory.unapply0(List.Nil.INSTANCE);
    }
}
