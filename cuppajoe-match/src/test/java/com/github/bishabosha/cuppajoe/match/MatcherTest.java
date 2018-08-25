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
            Option.of(6.28),
            Match(3.14).match(
                With(eq(3.14), (Double x) ->
                    x * 2.0),
                With(eq(2.72), (Double x) ->
                    x / 2.0)
            )
        );
        Assertions.assertEquals(
            Option.of(3.25),
            Match(6.5).match(
                With(eq(3.14), (Double $x) ->
                    $x * 2.0),
                With(id(), (Double $x) ->
                    $x / 2.0)
            )
        );
        Assertions.assertEquals(
            Option.of(Math.PI),
            Match(3.14).match(
                With(in(3.14),
                    Math.PI),
                With(in(2.72),
                    Math.E),
                With(id(), $x ->
                    $x)
            )
        );
        var cases = Cases(
            With(eq("spicy"), x ->
                "Thats one " + x + " meme"),
            With(eq("hw"), x ->
                "Hello World")
        );
        Assertions.assertEquals(
            "Hello World",
            Match("hw").get(cases)
        );
        Assertions.assertEquals(
            "Thats one spicy meme",
            Match("spicy").get(cases)
        );
        Assertions.assertEquals(
            "None Found",
            Match("jsdkjfksj").match(cases).orElse("None Found")
        );
    }

    @Test
    public void test_OnList() {
        var cases = Cases(
            With(in("-h", "--help"),
                "View Help"),
            With(in("-v", "--version"),
                "View Version")
        );
        Case<Object, String> numCases = Cases(
            With(in(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE),
                "One"),
            With(in(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)),
                "Two"),
            With(in(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)),
                "Three")
        );
        Assertions.assertEquals(
            "View Help",
            Match("-h").get(cases)
        );
        Assertions.assertEquals(
            "View Help",
            Match("--help").get(cases)
        );
        Assertions.assertEquals(
            "View Version",
            Match("-v").get(cases)
        );
        Assertions.assertEquals(
            "View Version",
            Match("--version").get(cases)
        );
        Assertions.assertEquals(
            "Malformatted Args",
            Match("-u").match(cases).orElse("Malformatted Args")
        );
        Assertions.assertEquals(
            "One",
            numCases.get(BigInteger.ONE)
        );
        Assertions.assertEquals(
            "Two",
            numCases.get(BigDecimal.valueOf(2))
        );
        Assertions.assertEquals(
            "Three",
            numCases.get("3.0")
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
            Option.of(1),
            Match(tree).match(
                With(inode(id(), __(), __()), (Integer node) ->
                    node + 1
                )
            )
        );
        Assertions.assertEquals(
            Tree.Node(1, Tree.Leaf(), Tree.Leaf()),
            Match(tree).get(
                With(inode(__(), __(), id()), (Tree<Integer> right) ->
                    right
                )
            )
        );
        Assertions.assertEquals(
            Option.of(0),
            Match(tree).match(
                With(inode(__(), id(), id()), this::sumNodes)
            )
        );
        Assertions.assertEquals(
            tree,
            Match(tree).get(
                With(inode(id(), id(), id()), this::makeTree)
            )
        );
        Assertions.assertEquals(
            Option.of(25),
            Match(leaf).match(
                With(inode(id(), leaf(), leaf()), (Integer node) ->
                    node
                )
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
        return Match(Tuple(x, y)).get(
            With(tuple(leaf(), leaf()),
                0
            ),
            With(tuple(inode(id(), __(), __()), inode(id(), __(), __())),  (Integer nodeX, Integer nodeY) ->
                nodeX + nodeY
            ),
            With(tuple(leaf(), inode(id(), __(), __())),                  (Integer nodeY) ->
                nodeY
            ),
            With(tuple(inode(id(), __(), __()), leaf()),                  (Integer nodeX) ->
                nodeX
            )
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
            Match(option).get(
                With(some(id()), (Integer $x) ->
                    IfUnsafe(
                        When(() -> $x > 5, $x)
                    )
                )
            )
            .intValue()
        );
    }
}
