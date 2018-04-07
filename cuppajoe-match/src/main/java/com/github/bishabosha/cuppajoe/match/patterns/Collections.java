package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> Node$(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFactory.unapply3(Tree.Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> Leaf$() {
        return PatternFactory.unapply0(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> Cons$(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFactory.unapply2(List.Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> Nil$() {
        return PatternFactory.unapply0(List.Nil.INSTANCE);
    }
}
