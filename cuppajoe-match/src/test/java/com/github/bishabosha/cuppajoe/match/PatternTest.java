/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.API;
import com.github.bishabosha.cuppajoe.collections.immutable.List;
import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Result;
import com.github.bishabosha.cuppajoe.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import static com.github.bishabosha.cuppajoe.match.Case.with;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.None_;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.Some_;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.Tuple2$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.__;
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
            API.Some(Result.of(0)),
            INode_($(0), __(), __()).test(tree)
        );
        assertEquals(
            API.Some(Result.of(0)),
            INode_($(), __(), __()).test(tree)
        );
        assertEquals(
            API.None(),
            INode_($(5), __(), __()).test(tree)
        );
        assertEquals(
            API.Some(Result.of(com.github.bishabosha.cuppajoe.collections.immutable.API.Tree(1))),
            INode_(__(), __(), $()).test(tree)
        );
        assertEquals(
            API.None(),
            INode_($(), $(null), $(null)).test(tree)
        );
    }

    private static Pattern<Tree<Integer>> INode_(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.Node$(node, left, right);
    }

    @Test
    public void test_flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = Tuple2$(Some_(INode_($(), __(), $())), $());

        underTest = API.Tuple(Option.of(Tree.Node(1, Tree.Leaf(), Tree.Leaf())), com.github.bishabosha.cuppajoe.collections.immutable.API.List(com.github.bishabosha.cuppajoe.collections.immutable.API.Tree(2)));

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
        var cases = Case.combine(
                with(Some_(Tuple2$($(), $())), (x, y) -> "Some(Tuple(" + x + ", " + y + "))"),
                with(None_(), () -> "None")/*,
            with($Lazy($_()), () -> "will not compile") */
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(API.Some(API.Tuple(1, 2))));
        assertEquals("None", cases.get(API.None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
