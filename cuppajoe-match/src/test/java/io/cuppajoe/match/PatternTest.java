/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.patterns.Collections;
import io.cuppajoe.match.patterns.Pattern;
import io.cuppajoe.match.patterns.Result;
import io.cuppajoe.tuples.Tuple2;
import org.junit.Test;

import static io.cuppajoe.API.*;
import static io.cuppajoe.collections.immutable.API.List;
import static io.cuppajoe.collections.immutable.API.Tree;
import static io.cuppajoe.collections.immutable.Tree.Leaf;
import static io.cuppajoe.collections.immutable.Tree.Node;
import static io.cuppajoe.match.Case.with;
import static io.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternTest {

    Tree<Integer> getTree() {
        return Node(
            0,
            Node(
                -1,
                Node(
                    -2,
                    Leaf(),
                    Leaf()),
                Leaf()),
            Node(
                1,
                Leaf(),
                Leaf())
        );
    }

    @Test
    public void testTree() {

        var tree = getTree();

        assertEquals(
                Some(Result.of(0)),
                INode_($(0), __(), __()).test(tree)
        );
        assertEquals(
                Some(Result.of(0)),
                INode_($(), __(), __()).test(tree)
        );
        assertEquals(
                None(),
                INode_($(5), __(), __()).test(tree)
        );
        assertEquals(
                Some(Result.of(Tree(1))),
                INode_(__(), __(), $()).test(tree)
        );
        assertEquals(
                None(),
                INode_($(), $(null), $(null)).test(tree)
        );
    }

    private static Pattern<Tree<Integer>> INode_(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.Node$(node, left, right);
    }

    @Test
    public void flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = Tuple2$(Some_(INode_($(), __(), $())), $());

        underTest = Tuple(Option.of(Node(1, Leaf(), Leaf())), List(Tree(2)));

        patt2Test.test(underTest).peek(results -> {
            var values = results.values();
            assertEquals(3, results.size());
            assertEquals(1, (int) values.nextVal());
            assertEquals(Leaf(), values.nextVal());
            assertEquals(List.of(Tree.of(2)), values.nextVal());
        });
    }

    @Test
    public void typeSafety() {
        var cases = Case.combine(
                with(Some_(Tuple2$($(), $())), (x, y) -> "Some(Tuple(" + x + ", " + y + "))"),
                with(None_(), () -> "None")/*,
            with($Lazy($_()), () -> "will not compile") */
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
