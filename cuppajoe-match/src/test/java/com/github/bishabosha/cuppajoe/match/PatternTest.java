/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.collections.immutable.tuples.Tuple2;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.API.None;
import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.*;
import static com.github.bishabosha.cuppajoe.collections.immutable.Tree.Leaf;
import static com.github.bishabosha.cuppajoe.collections.immutable.Tree.Node;
import static com.github.bishabosha.cuppajoe.match.API.Cases;
import static com.github.bishabosha.cuppajoe.match.API.With;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
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
    public void test_Tree() {

        var tree = getTree();

        assertEquals(
            Some(List.singleton(0)),
            inode(eq(0), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            Some(List.singleton(0)),
            inode(id(), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            None(),
            inode(eq(5), __(), __()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            Some(List.singleton(Tree(1))),
            inode(__(), __(), id()).test(tree).map(ListCapture::capture)
        );
        assertEquals(
            None(),
            inode(id(), eq(null), eq(null)).test(tree)
        );
    }

    private static Pattern<Tree<Integer>> inode(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return node(node, left, right);
    }

    @Test
    public void test_flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = tuple(some(inode(id(), __(), id())), id());

        underTest = Tuple(Some(Node(1, Leaf(), Leaf())), List(Tree(2)));

        var listOp = patt2Test.test(underTest).map(ListCapture::capture);

        assertFalse(listOp.isEmpty());

        var list = listOp.get();

        assertEquals(1, list.head());
        assertEquals(Leaf(), list.tail().head());
        assertEquals(List.singleton(Tree.ofAll(2)), list.tail().tail().head());
    }

    @Test
    public void test_typeSafety() {
        var cases = Cases(
            With(some(tuple(id(), id())), (x, y) ->
                "Some(Tuple(" + x + ", " + y + "))"),
            With(none(), () ->
                "None")/*,
            With(Lazy$($()), () -> "will not compile")*/
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
