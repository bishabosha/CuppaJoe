/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import com.bishabosha.caffeine.functional.immutable.Tree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static com.bishabosha.caffeine.functional.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.Option.Some;
import static com.bishabosha.caffeine.functional.Pattern.*;
import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;
import static com.bishabosha.caffeine.functional.tuples.Tuple2.Tuple;
import static com.bishabosha.caffeine.functional.immutable.Tree.*;

public class MatcherTest {

    @Test
    void testBasic() {
        Assertions.assertEquals(
            Option.of(6.28),
            match(3.14).option(
                with($(3.14), (Double x) -> x * 2.0),
                with($(2.72), (Double x) -> x / 2.0)
            )
        );
        Assertions.assertEquals(
            Option.of(3.25),
            match(6.5).option(
                with($(3.14),   (Double x) -> x * 2.0),
                with($x,        (Double $x) -> $x / 2.0)
            )
        );
        Assertions.assertEquals(
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
        Assertions.assertEquals(
            "Hello World",
            match("hw").of(cases)
        );
        Assertions.assertEquals(
            "Thats one spicy meme",
            match("spicy").of(cases)
        );
        Assertions.assertEquals(
            "Nothing Found",
            match("jsdkjfksj").option(cases).orElse("Nothing Found")
        );
    }

    @Test
    void testOnList() {
        Case cases = Case.combine(
            with($any("-h", "--help"), () -> "View Help"),
            with($any("-v", "--version"), () -> "View Version")
        );
        Case numCases = Case.combine(
            with($any(1, 1L, 1.0, "1", "1.0", BigInteger.ONE, BigDecimal.ONE), () -> "One"),
            with($any(2, 2L, 2.0, "2", "2.0", BigInteger.valueOf(2), BigDecimal.valueOf(2)), () -> "Two"),
            with($any(3, 3L, 3.0, "3", "3.0", BigInteger.valueOf(3), BigDecimal.valueOf(3)), () -> "Three")
        );
        Assertions.assertEquals(
            "View Help",
            match("-h").of(cases)
        );
        Assertions.assertEquals(
            "View Help",
            match("--help").of(cases)
        );
        Assertions.assertEquals(
            "View Version",
            match("-v").of(cases)
        );
        Assertions.assertEquals(
            "View Version",
            match("--version").of(cases)
        );
        Assertions.assertEquals(
            "Malformatted Args",
            match("-u").option(cases).orElse("Malformatted Args")
        );
        Assertions.assertEquals(
            "One",
            match(BigInteger.ONE).of(numCases)
        );
        Assertions.assertEquals(
            "Two",
            match(BigDecimal.valueOf(2)).of(numCases)
        );
        Assertions.assertEquals(
            "Three",
            match("3.0").of(numCases)
        );
    }

    Tree<Integer> getTree() {
        return Tree(
            0,
            Tree(
            -1,
                Tree(
                -2,
                    Leaf(),
                    Leaf()),
                Leaf()),
            Tree(
                1,
                Leaf(),
                Leaf())
        );
    }

    @Test
    void testPatterns() {

        Tree<Integer> tree = getTree();
        Tree<Integer> leaf = Tree(25, Leaf(), Leaf());

        Assertions.assertEquals(
            Option.of(1),
            match(tree).option(
                with(Node($x, ¥_, ¥_), (Integer $x) -> $x + 1)
            )
        );
        Assertions.assertEquals(
            Tree(1, Leaf(), Leaf()),
            match(tree).option(
                with(¥n_¥l_$r, $r -> $r)
            ).get()
        );
        Assertions.assertEquals(
            Option.of(0),
            match(tree).option(
                with(Node(¥_, $x, $y), this::sumNodes)
            )
        );
        Assertions.assertEquals(
            tree,
            match(tree).of(
                with(Node($x, $y, $z), this::makeTree)
            )
        );
        Assertions.assertEquals(
            Option.of(25),
            match(leaf).option(
                with(Node($x, ¥leaf, ¥leaf), $x -> $x)
            )
        );
    }

    @Test
    void testRecursion() {
        Assertions.assertEquals(
            3,
            getTree().height()
        );
        Assertions.assertEquals(
            Leaf(),
            ((Tree<Integer>)Leaf()).remove(78)
        );
        Assertions.assertEquals(
            getTree(),
            getTree()
        );
        Tree<String> hello = ((Tree<String>)Leaf()).add("Hello");
        Assertions.assertEquals(
            Tree("Hello", Leaf(), Leaf()),
            hello
        );
    }

    Tree<Integer> makeTree(int x, Tree<Integer> left, Tree<Integer> right) {
        return Tree(x, left, right);
    }

    int sumNodes(Tree<Integer> x, Tree<Integer> y) {
        return match(Tuple(x, y)).of(
            with(Tuple(¥leaf, ¥leaf),                               () -> 0),
            with(Tuple($n_¥l_¥r, $n_¥l_¥r), (Integer $n1, Integer $n2) -> $n1 + $n2),
            with(Tuple(¥leaf, $n_¥l_¥r),                  (Integer $n) -> $n),
            with(Tuple($n_¥l_¥r, ¥leaf),                  (Integer $n) -> $n)
        );
    }

    @Test
    void testGuards() {
        int x = 3;
        Assertions.assertEquals(
            "Three",
            guardUnsafe(
                when(() -> x == 3, () -> "Three"),
                edge(() -> "?")
            )
        );
        Assertions.assertEquals(
            "?",
            guardUnsafe(
                when(() -> 1 < 0, () -> "impossible"),
                edge(() -> "?")
            )
        );
    }

    @Test
    void testPredicates() {
        Option<Integer> option = Option.of(10);
        Assertions.assertEquals(
            10,
                (int) match(option).of(
                with(Some($x), (Integer $x) -> guardUnsafe(
                    when(() -> $x > 5, () -> $x)
                ))
            )
        );
    }
}
