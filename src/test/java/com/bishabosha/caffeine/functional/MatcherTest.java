/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.immutable.Tree;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.bishabosha.caffeine.functional.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.Option.Some;
import static com.bishabosha.caffeine.functional.Pattern.*;
import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.API.Tuple;
import static com.bishabosha.caffeine.functional.tuples.Tuple2.Tuple2;
import static com.bishabosha.caffeine.functional.immutable.Tree.*;

public class MatcherTest {

    @Test
    public void testBasic() {
        Assert.assertEquals(
            Option.of(6.28),
            match(3.14).option(
                with($(3.14), (Double x) -> x * 2.0),
                with($(2.72), (Double x) -> x / 2.0)
            )
        );
        Assert.assertEquals(
            Option.of(3.25),
            match(6.5).option(
                with($(3.14), (Double x) -> x * 2.0),
                with($x,     (Double $x) -> $x / 2.0)
            )
        );
        Assert.assertEquals(
            Option.of(Math.PI),
            match(3.14).option(
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
            match("hw").of(cases)
        );
        Assert.assertEquals(
            "Thats one spicy meme",
            match("spicy").of(cases)
        );
        Assert.assertEquals(
            "Nothing Found",
            match("jsdkjfksj").option(cases).orElse("Nothing Found")
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
            match("-h").of(cases)
        );
        Assert.assertEquals(
            "View Help",
            match("--help").of(cases)
        );
        Assert.assertEquals(
            "View Version",
            match("-v").of(cases)
        );
        Assert.assertEquals(
            "View Version",
            match("--version").of(cases)
        );
        Assert.assertEquals(
            "Malformatted Args",
            match("-u").option(cases).orElse("Malformatted Args")
        );
        Assert.assertEquals(
            "One",
            match(BigInteger.ONE).of(numCases)
        );
        Assert.assertEquals(
            "Two",
            match(BigDecimal.valueOf(2)).of(numCases)
        );
        Assert.assertEquals(
            "Three",
            match("3.0").of(numCases)
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
            match(tree).option(
                with(Node($x, ¥_, ¥_), (Integer $x) -> $x + 1)
            )
        );
        Assert.assertEquals(
            Tree.Node(1, leaf(), leaf()),
            match(tree).option(
                with(Node(¥_, ¥_, $r), $r -> $r)
            ).get()
        );
        Assert.assertEquals(
            Option.of(0),
            match(tree).option(
                with(Node(¥_, $x, $y), this::sumNodes)
            )
        );
        Assert.assertEquals(
            tree,
            match(tree).of(
                with(Node($x, $y, $z), this::makeTree)
            )
        );
        Assert.assertEquals(
            Option.of(25),
            match(leaf).option(
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
        return match(Tuple(x, y)).of(
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
                (int) match(option).of(
                with(Some($x), (Integer $x) -> guardUnsafe(
                    when(() -> $x > 5, () -> $x)
                ))
            )
        );
    }
}
