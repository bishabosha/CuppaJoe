/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.control.Option;
import com.bishabosha.caffeine.functional.immutable.Tree;
import com.bishabosha.caffeine.functional.patterns.Case;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.bishabosha.caffeine.functional.API.Match;
import static com.bishabosha.caffeine.functional.patterns.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.control.Option.Some;
import static com.bishabosha.caffeine.functional.patterns.Pattern.*;
import static com.bishabosha.caffeine.functional.patterns.Case.*;
import static com.bishabosha.caffeine.functional.API.Tuple;
import static com.bishabosha.caffeine.functional.tuples.Tuple2.Tuple2;
import static com.bishabosha.caffeine.functional.immutable.Tree.*;

public class MatcherTest {

    @Test
    public void testBasic() {
        Assert.assertEquals(
            Option.of(6.28),
            Match(3.14).option(
                with($(3.14), (Double x) -> x * 2.0),
                with($(2.72), (Double x) -> x / 2.0)
            )
        );
        Assert.assertEquals(
            Option.of(3.25),
            Match(6.5).option(
                with($(3.14), (Double x) -> x * 2.0),
                with($x,     (Double $x) -> $x / 2.0)
            )
        );
        Assert.assertEquals(
            Option.of(Math.PI),
            Match(3.14).option(
                with($(3.14), () -> Math.PI),
                with($(2.72), () -> Math.E),
                with($x, $x -> $x)
            )
        );
        Case cases = Case.combine(
            with($("spicy"), x -> "Thats one " + x + " meme"),
            with($("hw"), x -> "Hello World")
        );
        Assert.assertEquals(
            "Hello World",
            Match("hw").of(cases)
        );
        Assert.assertEquals(
            "Thats one spicy meme",
            Match("spicy").of(cases)
        );
        Assert.assertEquals(
            "Nothing Found",
            Match("jsdkjfksj").option(cases).orElse("Nothing Found")
        );
    }

    @Test
    public void testOnList() {
        Case cases = Case.combine(
            with($any("-h", "--help"), () -> "View Help"),
            with($any("-v", "--version"), () -> "View Version")
        );
        Case numCases = Case.combine(
            with($any(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE), () -> "One"),
            with($any(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)), () -> "Two"),
            with($any(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)), () -> "Three")
        );
        Assert.assertEquals(
            "View Help",
            Match("-h").of(cases)
        );
        Assert.assertEquals(
            "View Help",
            Match("--help").of(cases)
        );
        Assert.assertEquals(
            "View Version",
            Match("-v").of(cases)
        );
        Assert.assertEquals(
            "View Version",
            Match("--version").of(cases)
        );
        Assert.assertEquals(
            "Malformatted Args",
            Match("-u").option(cases).orElse("Malformatted Args")
        );
        Assert.assertEquals(
            "One",
            Match(BigInteger.ONE).of(numCases)
        );
        Assert.assertEquals(
            "Two",
            Match(BigDecimal.valueOf(2)).of(numCases)
        );
        Assert.assertEquals(
            "Three",
            Match("3.0").of(numCases)
        );
    }

    Tree<Integer> getTree() {
        return Tree.Node(
            0,
            Tree.Node(
            -1,
                Tree.Node(
                -2,
                    leaf(),
                    leaf()),
                leaf()),
            Tree.Node(
                1,
                leaf(),
                leaf())
        );
    }

    @Test
    public void testPatterns() {

        Tree<Integer> tree = getTree();
        Tree<Integer> leaf = Tree.Node(25, leaf(), leaf());

        Assert.assertEquals(
            Option.of(1),
            Match(tree).option(
                with(Node($x, ¥_, ¥_), (Integer $x) -> $x + 1)
            )
        );
        Assert.assertEquals(
            Tree.Node(1, leaf(), leaf()),
            Match(tree).option(
                with(Node(¥_, ¥_, $r), $r -> $r)
            ).get()
        );
        Assert.assertEquals(
            Option.of(0),
            Match(tree).option(
                with(Node(¥_, $x, $y), this::sumNodes)
            )
        );
        Assert.assertEquals(
            tree,
            Match(tree).of(
                with(Node($x, $y, $z), this::makeTree)
            )
        );
        Assert.assertEquals(
            Option.of(25),
            Match(leaf).option(
                with(Node($x, Leaf(), Leaf()), $x -> $x)
            )
        );
    }

    @Test
    public void testRecursion() {
        Assert.assertEquals(
            3,
            getTree().height()
        );
        Assert.assertEquals(
            leaf(),
            ((Tree<Integer>) leaf()).remove(78)
        );
        Assert.assertEquals(
            getTree(),
            getTree()
        );
        Tree<String> hello = ((Tree<String>) leaf()).add("Hello");
        Assert.assertEquals(
            Tree.Node("Hello", leaf(), leaf()),
            hello
        );
    }

    Tree<Integer> makeTree(int x, Tree<Integer> left, Tree<Integer> right) {
        return Tree.Node(x, left, right);
    }

    int sumNodes(Tree<Integer> x, Tree<Integer> y) {
        return Match(Tuple(x, y)).of(
            with(Tuple2(Leaf(), Leaf()),                                             () -> 0),
            with(Tuple2(Node($n, ¥_, ¥_), Node($n, ¥_, ¥_)), (Integer $n1, Integer $n2) -> $n1 + $n2),
            with(Tuple2(Leaf(), Node($n, ¥_, ¥_)),                         (Integer $n) -> $n),
            with(Tuple2(Node($n, ¥_, ¥_), Leaf()),                         (Integer $n) -> $n)
        );
    }

    @Test
    public void testGuards() {
        int x = 3;
        Assert.assertEquals(
            "Three",
            guardUnsafe(
                when(() -> x == 3, () -> "Three"),
                edge(() -> "?")
            )
        );
        Assert.assertEquals(
            "?",
            guardUnsafe(
                when(() -> 1 < 0, () -> "impossible"),
                edge(() -> "?")
            )
        );
    }

    @Test
    public void testPredicates() {
        Option<Integer> option = Option.of(10);
        Assert.assertEquals(
            10,
                (int) Match(option).of(
                with(Some($x), (Integer $x) -> guardUnsafe(
                    when(() -> $x > 5, () -> $x)
                ))
            )
        );
    }
}
