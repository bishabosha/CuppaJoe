/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.trees.BinaryNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.bishabosha.caffeine.functional.Pattern.*;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;

public class PatternTest {

    static Pattern Tree(Pattern node, Pattern left, Pattern right) {
        return patternFor(BinaryNode.class).testThree(
            Tuple(node, BinaryNode::getValue),
            Tuple(left, BinaryNode::getLeft),
            Tuple(right, BinaryNode::getRight)
        );
    }

    Pattern Tree = patternFor(BinaryNode.class).build();

    @Test
    void testTree() {

        BinaryNode<Integer> tree = new BinaryNode<>(0);
        tree.addLeft(new BinaryNode<>(-1));
        tree.addRight(new BinaryNode<>(1));
        tree.getLeft().addLeft(new BinaryNode<>(-2));

        BinaryNode<Integer> leaf = new BinaryNode<>(25);

        Assertions.assertEquals(
            Option.nothing(),
            Tree.test(0)
        );
        Assertions.assertIterableEquals(
            Tuple(new BinaryNode<>(-1), new BinaryNode<>(1)),
            Tree(¥_, Tree, Tree).test(tree).get()
        );
        Assertions.assertIterableEquals(
            Tuple(0),
            Tree($(0), ¥_, ¥_).test(tree).get()
        );
        Assertions.assertIterableEquals(
            Tuple(0),
            Tree($a, ¥_, ¥_).test(tree).get()
        );
        Assertions.assertEquals(
            Option.nothing(),
            Tree($(5), ¥_, ¥_).test(tree)
        );
        Assertions.assertIterableEquals(
            Tuple(new BinaryNode<>(1)),
            Tree(¥_, ¥_, Tree).test(tree).get()
        );
        Assertions.assertIterableEquals(
            Tuple(new BinaryNode<>(-2), null),
            Tree(¥_, Tree(¥_, Tree, ¥nil), ¥_).test(tree).get()
        );
        Assertions.assertEquals(
            Option.nothing(),
            Tree($a, ¥nil, ¥nil).test(tree)
        );
        Assertions.assertIterableEquals(
            Tuple(25, null, null),
            Tree($a, ¥nil, ¥nil).test(leaf).get()
        );
    }
}
