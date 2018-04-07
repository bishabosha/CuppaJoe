package com.github.bishabosha.cuppajoe.match.patterns;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;

import static com.github.bishabosha.cuppajoe.match.API.PatternFor;

public final class Collections {

    public static <O extends Comparable<O>> Pattern<Tree<O>> Node_(Pattern<O> $node, Pattern<Tree<O>> $left, Pattern<Tree<O>> $right) {
        return PatternFor(Tree.Node.class, $node, $left, $right);
    }

    public static <O extends Comparable<O>> Pattern<Tree<O>> Leaf_() {
        return PatternFor(Tree.Leaf.INSTANCE);
    }

    public static <O> Pattern<List<O>> Cons_(Pattern<O> $x, Pattern<List<O>> $xs) {
        return PatternFor(List.Cons.class, $x, $xs);
    }

    public static <O> Pattern<List<O>> Nil_() {
        return PatternFor(List.Nil.INSTANCE);
    }
}
