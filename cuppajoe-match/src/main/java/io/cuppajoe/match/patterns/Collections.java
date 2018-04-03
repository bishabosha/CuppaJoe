package io.cuppajoe.match.patterns;

import io.cuppajoe.Lazy;
import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.List.Cons;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.immutable.Tree.Node;
import io.cuppajoe.control.Option;
import io.cuppajoe.control.Option.Some;
import io.cuppajoe.control.Try.Failure;
import io.cuppajoe.control.Try.Success;
import io.cuppajoe.match.PatternFactory;

public final class Collections {

    public static Pattern $Node(Pattern $node, Pattern $left, Pattern $right) {
        return PatternFactory.gen3(Node.class, $node, $left, $right);
    }

    public static final Pattern $Leaf = PatternFactory.gen0(Tree.Leaf.INSTANCE);

    public static Pattern $Cons(Pattern $x, Pattern $xs) {
        return PatternFactory.gen2(Cons.class, $x, $xs);
    }

    public static final Pattern $Nil = PatternFactory.gen0(List.Nil.INSTANCE);

    public static Pattern $Success(Pattern $value) {
        return PatternFactory.gen1(Success.class, $value);
    }

    public static Pattern $Failure(Pattern $error) {
        return PatternFactory.gen1(Failure.class, $error);
    }

    public static Pattern $Lazy(Pattern $value) {
        return PatternFactory.gen1(Lazy.class, $value);
    }

    public static Pattern $Some(Pattern $value) {
        return PatternFactory.gen1(Some.class, $value);
    }

    public static final Pattern $None = PatternFactory.gen0(Option.None.INSTANCE);
}
