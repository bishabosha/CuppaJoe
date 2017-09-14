/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.immutable.Cons;
import com.bishabosha.caffeine.functional.immutable.Tree;
import com.bishabosha.caffeine.functional.patterns.Pattern;
import com.bishabosha.caffeine.functional.patterns.PatternResult;
import com.bishabosha.caffeine.functional.tuples.Tuple2;
import com.bishabosha.caffeine.trees.BinaryNode;
import org.junit.Test;

import static com.bishabosha.caffeine.functional.control.Option.Some;
import static com.bishabosha.caffeine.functional.patterns.Pattern.*;
import static com.bishabosha.caffeine.functional.patterns.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.immutable.Tree.Node;
import static com.bishabosha.caffeine.functional.immutable.Tree.leaf;
import static com.bishabosha.caffeine.functional.API.Tuple;
import static org.junit.Assert.*;

public class PatternTest {

    static Pattern tree(Pattern node, Pattern left, Pattern right) {
        return patternFor(BinaryNode.class).testThree(
            Tuple(node, BinaryNode::getValue),
            Tuple(left, BinaryNode::getLeft),
            Tuple(right, BinaryNode::getRight)
        );
    }

    Pattern tree = patternFor(BinaryNode.class).build();

    @Test
    public void testTree() {

        BinaryNode<Integer> tree = new BinaryNode<>(0);
        tree.addLeft(new BinaryNode<>(-1));
        tree.addRight(new BinaryNode<>(1));
        tree.getLeft().addLeft(new BinaryNode<>(-2));

        BinaryNode<Integer> leaf = new BinaryNode<>(25);

        assertEquals(
            Option.nothing(),
            this.tree.test(0)
        );
        assertEquals(
            PatternResult.of(0),
            tree($(0), ¥_, ¥_).test(tree).get()
        );
        assertEquals(
            PatternResult.of(0),
            tree($a, ¥_, ¥_).test(tree).get()
        );
        assertEquals(
            Option.nothing(),
            tree($(5), ¥_, ¥_).test(tree)
        );
        assertEquals(
            PatternResult.of(new BinaryNode<>(1)),
            tree(¥_, ¥_, this.tree).test(tree).get()
        );
        assertEquals(
            Option.nothing(),
            tree($a, ¥null, ¥null).test(tree)
        );
        assertEquals(
            PatternResult.of(25),
            tree($a, ¥null, ¥null).test(leaf).get()
        );
    }

    @Test
    public void flattenStress() {
        final Pattern patt2Test;
        final Tuple2<Option<Tree<Integer>>, Cons<Tree<Integer>>> underTest;

        patt2Test = Tuple2.Tuple2(Some(Node($x, ¥_, $y)), $xs);
        underTest = Tuple(Option.of(Node(1, leaf(), leaf())), Cons.of(Tree.of(2)));

        patt2Test.test(underTest).ifSome(results -> {
            assertEquals(3, results.size());
            assertEquals(1, (int) results.get(0));
            assertEquals(leaf(), results.get(1));
            assertEquals(Cons.of(Tree.of(2)), results.get(2));
        });
    }
}
