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
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.*;
import static com.github.bishabosha.cuppajoe.collections.immutable.Tree.Node;
import static com.github.bishabosha.cuppajoe.match.API.Cases;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple2$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PatternTest {

    Tree<Integer> getTree() {
        return Node(
            0,
            Node(
                -1,
                Node(
                    -2,
                    Tree.Leaf(),
                    Tree.Leaf()),
                Tree.Leaf()),
            Node(
                1,
                Tree.Leaf(),
                Tree.Leaf())
        );
    }

    @Test
    public void test_Tree() {

        var tree = getTree();

        assertEquals(
            Some(List.singleton(0)),
            INode$($(0), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            Some(List.singleton(0)),
            INode$($(), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            None(),
            INode$($(5), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            Some(List.singleton(Tree(1))),
            INode$(__(), __(), $()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            None(),
            INode$($(), $(null), $(null)).test(tree)
        );
    }

    private static Pattern<Tree<Integer>> INode$(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.Node$(node, left, right);
    }

    @Test
    public void test_flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = Tuple2$(Some$(INode$($(), __(), $())), $());

        underTest = Tuple(Option.of(Node(1, Tree.Leaf(), Tree.Leaf())), List(Tree(2)));

        var listOp = patt2Test.test(underTest).map(ListCapture::capture);

        assertFalse(listOp.isEmpty());

        var list = listOp.get();

        assertEquals(1, list.head());
        assertEquals(Tree.Leaf(), list.tail().head());
        assertEquals(List.singleton(Tree.ofAll(2)), list.tail().tail().head());
    }

    @Test
    public void test_typeSafety() {
        var cases = Cases(
            With(Some$(Tuple2$($(), $())), (x, y) ->
                "Some(Tuple(" + x + ", " + y + "))"),
            With(None$(), () ->
                "None")/*,
            With(Lazy$($()), () -> "will not compile")*/
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
