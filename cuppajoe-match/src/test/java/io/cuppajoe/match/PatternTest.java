/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.mutable.trees.BinaryNode;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.patterns.Collections;
import io.cuppajoe.match.patterns.Pattern;
import io.cuppajoe.match.patterns.Standard;
import io.cuppajoe.tuples.Tuple2;
import org.junit.Test;

import java.util.Objects;

import static io.cuppajoe.API.*;
import static io.cuppajoe.collections.immutable.Tree.Node;
import static io.cuppajoe.collections.immutable.Tree.leaf;
import static io.cuppajoe.match.Case.with;
import static io.cuppajoe.match.patterns.Collections.$None;
import static io.cuppajoe.match.patterns.Collections.$Some;
import static io.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatternTest {

    static Pattern<BinaryNode<Integer>> tree(Pattern<Integer> node, Pattern<BinaryNode<Integer>> left, Pattern<BinaryNode<Integer>> right) {
        return binaryNode ->
            node.test(binaryNode.getValue()).flatMap(n ->
                left.test(binaryNode.getLeft()).flatMap(l ->
                    right.test(binaryNode.getRight()).map(r ->
                        Result.compose(n, l, r))));
    }

    static final Pattern<BinaryNode<Integer>> $BinaryNode = x -> Standard.bind(x);

    @Test
    public void testTree() {

        var tree = new BinaryNode<>(0);
        tree.addLeft(new BinaryNode<>(-1));
        tree.addRight(new BinaryNode<>(1));
        tree.getLeft().addLeft(new BinaryNode<>(-2));

        var leaf = new BinaryNode<>(25);

        assertEquals(
            Result.of(0),
            tree($varEq(0), $_(), $_()).test(tree).get()
        );
        assertEquals(
            Result.of(0),
            tree($x(), $_(), $_()).test(tree).get()
        );
        assertEquals(
            None(),
            tree($varEq(5), $_(), $_()).test(tree)
        );
        assertEquals(
            Result.of(new BinaryNode<>(1)),
            tree($_(), $_(), this.$BinaryNode).test(tree).get()
        );
        assertEquals(
            None(),
            tree($x(), $eq(null), $eq(null)).test(tree)
        );
        assertTrue(
            Objects.equals(25, tree($x(), $eq(null), $eq(null)).test(leaf).get().values().nextVal())
        );
    }

    private static Pattern<Tree<Integer>> $INode(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.$Node(node, left, right);
    }

    @Test
    public void flattenStress() {
        final Pattern<Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>>> patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = $Tuple2($Some($INode($x(), $_(), $x())), $x());

        underTest = Tuple(Option.of(Node(1, leaf(), leaf())), List(Tree(2)));

        patt2Test.test(underTest).peek(results -> {
            var values = results.values();
            assertEquals(3, results.size());
            assertEquals(1, (int) values.nextVal());
            assertEquals(leaf(), values.nextVal());
            assertEquals(List.of(Tree.of(2)), values.nextVal());
        });
    }

    @Test
    public void typeSafety() {
        var cases = Case.combine(
            with($Some($Tuple2($x(), $x())), (x, y) -> "Some(Tuple(" + x + ", " + y + "))"),
            with($None(), () -> "None")/*,
            with($Lazy($_()), () -> "will not compile") */
        );

        assertEquals("Some(Tuple(1, 2))", cases.get(Some(Tuple(1, 2))));
        assertEquals("None", cases.get(None()));
//      assertEquals("will not compile", cases.get(Lazy(() -> 0)));
    }
}
