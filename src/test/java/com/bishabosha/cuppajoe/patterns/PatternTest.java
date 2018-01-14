/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.patterns;

import com.bishabosha.cuppajoe.collections.immutable.List;
import com.bishabosha.cuppajoe.collections.immutable.Tree;
import com.bishabosha.cuppajoe.collections.mutable.trees.BinaryNode;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import org.junit.Test;

import static com.bishabosha.cuppajoe.API.Nothing;
import static com.bishabosha.cuppajoe.API.Tuple;
import static com.bishabosha.cuppajoe.collections.immutable.Tree.Node;
import static com.bishabosha.cuppajoe.collections.immutable.Tree.Node.$Node;
import static com.bishabosha.cuppajoe.collections.immutable.Tree.leaf;
import static com.bishabosha.cuppajoe.control.Some.$Some;
import static com.bishabosha.cuppajoe.patterns.Pattern.*;
import static com.bishabosha.cuppajoe.tuples.Tuple2.$Tuple2;
import static org.junit.Assert.assertEquals;

public class PatternTest {

    static Pattern tree(Pattern node, Pattern left, Pattern right) {
        return x -> {
            if (x instanceof BinaryNode) {
                BinaryNode binaryNode = (BinaryNode) x;
                return node.test(binaryNode.getValue()).flatMap(
                    n -> left.test(binaryNode.getLeft()).flatMap(
                        l -> right.test(binaryNode.getRight()).map(
                            r -> Result.compose(n, l, r))));
            }
            return Nothing();
        };
    }

    Pattern tree = x -> x instanceof BinaryNode ? Pattern.bind(x) : Pattern.FAIL;

    @Test
    public void testTree() {

        BinaryNode<Integer> tree = new BinaryNode<>(0);
        tree.addLeft(new BinaryNode<>(-1));
        tree.addRight(new BinaryNode<>(1));
        tree.getLeft().addLeft(new BinaryNode<>(-2));

        BinaryNode<Integer> leaf = new BinaryNode<>(25);

        assertEquals(
            Nothing(),
            this.tree.test(0)
        );
        assertEquals(
            Result.of(0),
            tree($(0), ¥_, ¥_).test(tree).get()
        );
        assertEquals(
            Result.of(0),
            tree($a, ¥_, ¥_).test(tree).get()
        );
        assertEquals(
            Nothing(),
            tree($(5), ¥_, ¥_).test(tree)
        );
        assertEquals(
            Result.of(new BinaryNode<>(1)),
            tree(¥_, ¥_, this.tree).test(tree).get()
        );
        assertEquals(
            Nothing(),
            tree($a, ¥null, ¥null).test(tree)
        );
        assertEquals(
            Result.of(25),
            tree($a, ¥null, ¥null).test(leaf).get()
        );
    }

    @Test
    public void flattenStress() {
        final Pattern patt2Test;
        final Product2<Option<Tree<Integer>>, List<Tree<Integer>>> underTest;

        patt2Test = $Tuple2($Some($Node($x, ¥_, $y)), $xs);
        underTest = Tuple(Option.of(Node(1, leaf(), leaf())), List.of(Tree.of(2)));

        patt2Test.test(underTest).ifSome(results -> {
            Result.Values values = results.values();
            assertEquals(3, results.size());
            assertEquals(1, (int) values.next());
            assertEquals(leaf(), values.next());
            assertEquals(List.of(Tree.of(2)), values.next());
        });
    }
}
