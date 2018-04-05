/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match;

import io.cuppajoe.collections.immutable.Tree;
import io.cuppajoe.control.Option;
import io.cuppajoe.match.patterns.Collections;
import io.cuppajoe.match.patterns.Pattern;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.cuppajoe.API.Tuple;
import static io.cuppajoe.collections.immutable.Tree.Leaf;
import static io.cuppajoe.collections.immutable.Tree.Node;
import static io.cuppajoe.match.API.Match;
import static io.cuppajoe.match.Case.*;
import static io.cuppajoe.match.Matcher.guardUnsafe;
import static io.cuppajoe.match.patterns.Collections.Leaf$;
import static io.cuppajoe.match.patterns.Standard.*;

public class MatcherTest {

    private static Pattern<Tree<Integer>> INode$(Pattern<Integer> node, Pattern<Tree<Integer>> left, Pattern<Tree<Integer>> right) {
        return Collections.Node$(node, left, right); // ensure comparable is inferred to wildcards
    }

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
                with($(), (Double $x) -> $x / 2.0)
            )
        );
        Assert.assertEquals(
            Option.of(Math.PI),
            Match(3.14).option(
                with($(3.14), () -> Math.PI),
                with($(2.72), () -> Math.E),
                with($(), $x -> $x)
            )
        );
        var cases = Case.combine(
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
            "None Found",
            Match("jsdkjfksj").option(cases).orElse("None Found")
        );
    }

    @Test
    public void testOnList() {
        var cases = Case.combine(
            with($any("-h", "--help"), () -> "View Help"),
            with($any("-v", "--version"), () -> "View Version")
        );
        Case<Object, String> numCases = Case.combine(
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
            Match((Object) BigInteger.ONE).of(numCases)
        );
        Assert.assertEquals(
            "Two",
            Match((Object)BigDecimal.valueOf(2)).of(numCases)
        );
        Assert.assertEquals(
            "Three",
            Match((Object)"3.0").of(numCases)
        );
    }

    private Tree<Integer> getTree() {
        return Node(
            0,
            Node(
                -1,
                Node(
                    -2,
                    Leaf(),
                    Leaf()),
                Leaf()),
            Node(
                1,
                Leaf(),
                Leaf())
        );
    }

    @Test
    public void testPatterns() {

        var tree = getTree();
        var leaf = Node(25, Leaf(), Leaf());

        Assert.assertEquals(
            Option.of(1),
            Match(tree).option(
                with(INode$($(), __(), __()), (Integer $x) -> $x + 1)
            )
        );
        Assert.assertEquals(
            Node(1, Leaf(), Leaf()),
            Match(tree).of(
                with(INode$(__(), __(), $()), $r -> $r)
            )
        );
        Assert.assertEquals(
            Option.of(0),
            Match(tree).option(
                with(INode$(__(), $(), $()), this::sumNodes)
            )
        );
        Assert.assertEquals(
            tree,
            Match(tree).of(
                with(INode$($(), $(), $()), this::makeTree)
            )
        );
        Assert.assertEquals(
            Option.of(25),
            Match(leaf).option(
                with(INode$($(), Leaf$(), Leaf$()), $x -> $x)
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
            getTree(),
            getTree()
        );
        var hello = Tree.<String>Leaf().add("Hello");
        Assert.assertEquals(
            Node("Hello", Leaf(), Leaf()),
            hello
        );
    }

    private Tree<Integer> makeTree(int x, Tree<Integer> left, Tree<Integer> right) {
        return Node(x, left, right);
    }

    private int sumNodes(Tree<Integer> x, Tree<Integer> y) {
        return Match(Tuple(x, y)).of(
            with(Tuple2$(Leaf$(), Leaf$()), () -> 0),
            with(Tuple2$(INode$($(), __(), __()), INode$($(), __(), __())), (Integer $n1, Integer $n2) -> $n1 + $n2),
            with(Tuple2$(Leaf$(), INode$($(), __(), __())), (Integer $n) -> $n),
            with(Tuple2$(INode$($(), __(), __()), Leaf$()), (Integer $n) -> $n)
        );
    }

    @Test
    public void testGuards() {
        var x = 3;
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
        var option = Option.of(10);
        Assert.assertEquals(
            10,
            (int) Match(option).of(
                with(Some_($()), (Integer $x) -> guardUnsafe(
                    when(() -> $x > 5, () -> $x)
                ))
            )
        );
    }
}
