/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match;

import com.github.bishabosha.cuppajoe.collections.immutable.Tree;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.patterns.Collections;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.github.bishabosha.cuppajoe.API.Some;
import static com.github.bishabosha.cuppajoe.collections.immutable.API.Tuple;
import static com.github.bishabosha.cuppajoe.match.API.*;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Leaf$;
import static com.github.bishabosha.cuppajoe.match.patterns.Collections.Tuple2$;
import static com.github.bishabosha.cuppajoe.match.patterns.Standard.*;

public class MatcherTest {

    private static Pattern<Tree<Integer>> INode$(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.Node$(node, left, right); // ensure comparable is inferred to wildcards
    }

    @Test
    public void test_Basic() {
        Assertions.assertEquals(
            Option.of(6.28),
            Match(3.14).option(
                With ($(3.14), (Double x) ->
                    x * 2.0),
                With ($(2.72), (Double x) ->
                    x / 2.0)
            )
        );
        Assertions.assertEquals(
            Option.of(3.25),
            Match(6.5).option(
                With ($(3.14), (Double $x) ->
                    $x * 2.0),
                With ($(), (Double $x) ->
                    $x / 2.0)
            )
        );
        Assertions.assertEquals(
            Option.of(Math.PI),
            Match(3.14).option(
                With ($(3.14), () ->
                    Math.PI),
                With ($(2.72), () ->
                    Math.E),
                With ($(), $x ->
                    $x)
            )
        );
        var cases = Cases(
            With ($("spicy"), x ->
                "Thats one " + x + " meme"),
            With ($("hw"), x ->
                "Hello World")
        );
        Assertions.assertEquals(
            "Hello World",
            Match("hw").of(cases)
        );
        Assertions.assertEquals(
            "Thats one spicy meme",
            Match("spicy").of(cases)
        );
        Assertions.assertEquals(
            "None Found",
            Match("jsdkjfksj").option(cases).orElse("None Found")
        );
    }

    @Test
    public void test_OnList() {
        var cases = Cases(
            With($any("-h", "--help"), () ->
                "View Help"
            ),
            With($any("-v", "--version"), () ->
                "View Version"
            )
        );
        Case<Object, String> numCases = Cases(
            With($any(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE), () ->
                "One"
            ),
            With($any(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)), () ->
                "Two"
            ),
            With($any(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)), () ->
                "Three"
            )
        );
        Assertions.assertEquals(
            "View Help",
            Match("-h").of(cases)
        );
        Assertions.assertEquals(
            "View Help",
            Match("--help").of(cases)
        );
        Assertions.assertEquals(
            "View Version",
            Match("-v").of(cases)
        );
        Assertions.assertEquals(
            "View Version",
            Match("--version").of(cases)
        );
        Assertions.assertEquals(
            "Malformatted Args",
            Match("-u").option(cases).orElse("Malformatted Args")
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
            Match(tree).option(
                With(INode$($(), __(), __()), (Integer node) ->
                    node + 1
                )
            )
        );
        Assertions.assertEquals(
            Tree.Node(1, Tree.Leaf(), Tree.Leaf()),
            Match(tree).of(
                With(INode$(__(), __(), $()), (Tree<Integer> right) ->
                    right
                )
            )
        );
        Assertions.assertEquals(
            Option.of(0),
            Match(tree).option(
                With(INode$(__(), $(), $()), this::sumNodes)
            )
        );
        Assertions.assertEquals(
            tree,
            Match(tree).of(
                With(INode$($(), $(), $()), this::makeTree)
            )
        );
        Assertions.assertEquals(
            Option.of(25),
            Match(leaf).option(
                With(INode$($(), Leaf$(), Leaf$()), (Integer node) ->
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
        return Match(Tuple(x, y)).of(
            With(Tuple2$(Leaf$(), Leaf$()),                                  () ->
                0
            ),
            With(Tuple2$(INode$($(), __(), __()), INode$($(), __(), __())),  (Integer nodeX, Integer nodeY) ->
                nodeX + nodeY
            ),
            With(Tuple2$(Leaf$(), INode$($(), __(), __())),                  (Integer nodeY) ->
                nodeY
            ),
            With(Tuple2$(INode$($(), __(), __()), Leaf$()),                  (Integer nodeX) ->
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
                When(() -> x == 3, () -> "Three"),
                Edge(() -> "?")
            )
        );
        Assertions.assertEquals(
            Some("?"),
            If(
                When(() -> 1 < 0, () -> "impossible"),
                Edge(() -> "?")
            )
        );
    }

    @Test
    public void test_Predicates() {
        var option = Option.of(10);
        Assertions.assertEquals(
            10,
            (int) Match(option).of(
                With(Some$($()), (Integer $x) ->
                    IfUnsafe(
                        When(() -> $x > 5, () -> $x)
                    )
                )
            )
        );
    }
}
