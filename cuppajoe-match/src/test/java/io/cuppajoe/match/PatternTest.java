/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.collections.immutable.List;
import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.collections.mutable.trees.BinaryNode;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.patterns.Pattern;
import io.cuppajoe.match.patterns.Standard;
import io.cuppajoe.tuples.Tuple2;
import org.junit.Test;

import java.util.Objects;

import static io.cuppajoe.API.None;
import static io.cuppajoe.API.Tuple;
import static io.cuppajoe.collections.immutable.Tree.Node;
import static io.cuppajoe.collections.immutable.Tree.leaf;
import static io.cuppajoe.match.patterns.Collections.$Node;
import static io.cuppajoe.match.patterns.Collections.$Some;
import static io.cuppajoe.match.patterns.Standard.*;
import static org.junit.jupiter.api.Assertions.*;

public class PatternTest {

    static Pattern tree(Pattern node, Pattern left, Pattern right) {
        return x -> {
            if (x instanceof BinaryNode) {
                var binaryNode = (BinaryNode) x;
                return node.test(binaryNode.getValue()).flatMap(
                    n -> left.test(binaryNode.getLeft()).flatMap(
                        l -> right.test(binaryNode.getRight()).map(
                            r -> Result.compose(n, l, r))));
            }
            return None();
        };
    }

    Pattern tree = x -> x instanceof BinaryNode ? Standard.bind(x) : Standard.FAIL;

    @Test
    public void testTree() {

        var tree = new BinaryNode<>(0);
        tree.addLeft(new BinaryNode<>(-1));
        tree.addRight(new BinaryNode<>(1));
        tree.getLeft().addLeft(new BinaryNode<>(-2));

        var leaf = new BinaryNode<>(25);

        assertEquals(
            None(),
            this.tree.test(0)
        );
        assertEquals(
            Result.of(0),
            tree($(0), $_, $_).test(tree).get()
        );
        assertEquals(
            Result.of(0),
            tree($a, $_, $_).test(tree).get()
        );
        assertEquals(
            None(),
            tree($(5), $_, $_).test(tree)
        );
        assertEquals(
            Result.of(new BinaryNode<>(1)),
            tree($_, $_, this.tree).test(tree).get()
        );
        assertEquals(
            None(),
            tree($a, $_(null), $_(null)).test(tree)
        );
        assertTrue(
            Objects.equals(25, tree($a, $_(null), $_(null)).test(leaf).get().values().nextVal())
        );
    }

    @Test
    public void flattenStress() {
        final Pattern patt2Test;
        final Tuple2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = $Tuple2($Some($Node($x, $_, $y)), $xs);
        underTest = Tuple(Option.of(Node(1, leaf(), leaf())), List.of(Tree.of(2)));

        patt2Test.test(underTest).peek(results -> {
            var values = results.values();
            assertEquals(3, results.size());
            assertEquals(1, (int) values.nextVal());
            assertEquals(leaf(), values.nextVal());
            assertEquals(List.of(Tree.of(2)), values.nextVal());
        });
    }
}
