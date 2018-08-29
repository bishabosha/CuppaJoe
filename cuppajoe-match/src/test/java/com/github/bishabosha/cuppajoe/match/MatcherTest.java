/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.cases.Case;
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Standard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;

public class MatcherTest {

    private static Pattern<Tree<Integer>> inode(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.node(node, left, right); // ensure comparable is inferred to wildcards
    }

    @Test
    public void test_Basic() {
        Assertions.assertEquals(
            6.28,
            (double) MatchUnsafe(3.14, m -> m
                .with(eq(3.14))
                    .then((Double x) -> x * 2.0)
                .with(eq(2.72))
                    .then((Double x) -> x / 2.0)
            )
        );
        Assertions.assertEquals(
            3.25,
            (double) Match(6.5, m -> m
                .with(eq(3.14))
                    .then((Double $x) -> $x * 2.0)
                .def((Double $x) -> $x / 2.0)
            )
        );
        Assertions.assertEquals(
            Math.PI,
            (double) Match(3.14, m -> m
                .with(in(3.14))
                    .then(Math.PI)
                .with(in(2.72))
                    .then(Math.E)
                .def($x -> $x)
            )
        );
    }

    private Tree<Integer> getTree() {
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
    public void test_Patterns() {

        var tree = getTree();
        var leaf = Tree.Node(25, Tree.Leaf(), Tree.Leaf());

        Assertions.assertEquals(
            1,
            (int) MatchUnsafe(tree, m -> m
                .with(inode(id(), __(), __()))
                    .then((Integer node) -> node + 1)
            )
        );
        Assertions.assertEquals(
            Tree.Node(1, Tree.Leaf(), Tree.Leaf()),
            MatchUnsafe(tree, m -> m
                .with(inode(__(), __(), id()))
                    .then((Tree<Integer> right) -> right)
            )
        );
        Assertions.assertEquals(
            0,
            (int) MatchUnsafe(tree, m -> m
                .with(inode(__(), id(), id())).then(this::sumNodes)
            )
        );
        Assertions.assertEquals(
            tree,
            MatchUnsafe(tree, m -> m
                .with(inode(id(), id(), id())).then(this::makeTree)
            )
        );
        Assertions.assertEquals(
            25,
            (int) MatchUnsafe(leaf, m -> m
                .with(inode(id(), leaf(), leaf()))
                    .then((Integer node) -> node)
            )
        );
    }

    @Test
    public void test_Recursion() {
        Assertions.assertEquals(
            3,
            getTree().height()
        );
        Assertions.assertEquals(
            getTree(),
            getTree()
        );
        var hello = Tree.<String>Leaf().add("Hello");
        Assertions.assertEquals(
            Tree.Node("Hello", Tree.Leaf(), Tree.Leaf()),
            hello
        );
    }

    private Tree<Integer> makeTree(int x, Tree<Integer> left, Tree<Integer> right) {
        return Tree.Node(x, left, right);
    }

    private int sumNodes(Tree<Integer> x, Tree<Integer> y) {
        return MatchUnsafe(Tuple(x, y), m -> m
            .with(tuple(leaf(), leaf()))
                .then(0)
            .with(tuple(inode(id(), __(), __()), inode(id(), __(), __())))
                .then((Integer nodeX, Integer nodeY) -> nodeX + nodeY)
            .with(tuple(leaf(), inode(id(), __(), __())))
                .then((Integer nodeY) -> nodeY)
            .with(tuple(inode(id(), __(), __()), leaf()))
                .then((Integer nodeX) -> nodeX)
        );
    }

    @Test
    public void test_Guards() {
        var x = 3;
        Assertions.assertEquals(
            Some("Three"),
            If(
                When(() -> x == 3, "Three"),
                Edge("?")
            )
        );
        Assertions.assertEquals(
            Some("?"),
            If(
                When(() -> 1 < 0, "impossible"),
                Edge("?")
            )
        );
    }

    @Test
    public void test_Predicates() {
        var option = Option.of(10);
        Assertions.assertEquals(
            10,
            (int) MatchUnsafe(option, m -> m
                .with(some(id()))
                    .then((Integer $x) ->
                        IfUnsafe(
                            When(() -> $x > 5, $x)
                        )
                    )
            )
        );
    }
}
