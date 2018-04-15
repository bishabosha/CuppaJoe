/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.*;
import static com.github.bishabosha.cuppajoe.match.API.Cases;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple2_;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternTest {

    Tree<Integer> getTree() {
        return Tree.Node(
            0,
            Tree.Node(
                -1,
                Tree.Node(
                    -2,
                    Tree.Leaf(),
                    Tree.Leaf()),
                Tree.Leaf()),
            Tree.Node(
                1,
                Tree.Leaf(),
                Tree.Leaf())
        );
    }

    @Test
    public void test_Tree() {

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
        return Collections.Node_(node, left, right);
    }

    @Test
    public void test_flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = Tuple2_(Some_(INode_($(), __(), $())), $());

        underTest = Tuple(Option.of(Tree.Node(1, Tree.Leaf(), Tree.Leaf())), List(Tree(2)));

        patt2Test.test(underTest).peek(results -> {
            var values = results.values();
            assertEquals(3, results.size());
            assertEquals(1, (int) values.nextVal());
            assertEquals(Tree.Leaf(), values.nextVal());
            assertEquals(List.singleton(Tree.ofAll(2)), values.nextVal());
        });
    }

    @Test
    public void test_typeSafety() {
        var cases = Cases(
            With(Some_(Tuple2_($(), $())), (x, y) ->
                "Some(Tuple(" + x + ", " + y + "))"),
            With(None_(), () ->
                "None")/*,
            With(Lazy_($()), () -> "will not compile")*/
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
