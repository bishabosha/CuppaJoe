/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.immutable;

import com.bishabosha.caffeine.functional.Library;
import com.bishabosha.caffeine.functional.Option;
import com.bishabosha.caffeine.functional.Pattern;
import com.bishabosha.caffeine.functional.PatternResult;

import java.util.Objects;
import java.util.function.Predicate;

import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.Pattern.*;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.tuples.Tuples.Tuple;

/**
 * Immutable tree
 * @param <E> the type that the Tree contains
 */
public class Tree<E extends Comparable<E>> {

    /**
     * Singleton instance of the leaf node
     */
    private static final Tree<?> TREE_LEAF = new Tree<>(null, null, null);

    private static final Predicate<Tree> isLeaf =
        t -> t == TREE_LEAF || Library.allEqual(null, t.node, t.left, t.right);

    /**
     * This will check {@link Tree#node} {@link Tree#left} and {@link Tree#right} for <b>null</b> without binding.
     */
    public static final Pattern ¥leaf = x -> Option.of(x)
                                                   .cast(Tree.class)
                                                   .filter(isLeaf)
                                                   .flatMap(t -> Pattern.PASS);

    /**
     * For use after {@link Tree#¥leaf}. This will bind {@link Tree#node} without checks for null
     */
    public static final Pattern $n_¥l_¥r = x -> x instanceof Tree ? Pattern.bind(((Tree)x).node) : Pattern.FAIL;

    /**
     * For use after {@link Tree#¥leaf}. This will bind {@link Tree#left} without checks for null
     */
    public static final Pattern ¥n_$l_¥r = x -> x instanceof Tree ? Pattern.bind(((Tree)x).left) : Pattern.FAIL;

    /**
     * For use after {@link Tree#¥leaf}. This will bind {@link Tree#right} without checks for null
     */
    public static final Pattern ¥n_¥l_$r = x -> x instanceof Tree ? Pattern.bind(((Tree)x).right) : Pattern.FAIL;

    /**
     * For use after {@link Tree#¥leaf}. This will bind {@link Tree#node} {@link Tree#left} and {@link Tree#right} without checks for null
     */
    public static final Pattern $n_$l_$r = x -> {
        if (x instanceof Tree) {
            final Tree t = ((Tree)x);
            return Option.of(PatternResult.of(t.node, t.left, t.right));
        }
        return Pattern.FAIL;
    };

    /**
     * For use after {@link Tree#¥leaf}. This will bind {@link Tree#left} and {@link Tree#right} without checks for null
     */
    public static final Pattern ¥n_$l_$r = x -> {
        if (x instanceof Tree) {
            final Tree t = ((Tree)x);
            return Option.of(PatternResult.of(t.left, t.right));
        }
        return Pattern.FAIL;
    };

    /**
     * For use after {@link Tree#¥leaf}. This will apply patterns to {@link Tree#node} {@link Tree#left} and {@link Tree#right}
     * @param node The pattern to check the node
     * @param left The pattern to check the left sub tree
     * @param right The pattern to check the right sub tree
     * @return <b>Option&lt;PatternResult&gt;</b> if all patterns pass, otherwise {@link Option#nothing()}
     */
    public static Pattern Node(Pattern node, Pattern left, Pattern right) {
        return patternFor(Tree.class).testThree(
            Tuple(node, x -> x.node),
            Tuple(left, x -> x.left),
            Tuple(right, x -> x.right)
        );
    }

    private E node;
    private Tree<E> left;
    private Tree<E> right;

    public static <R extends Comparable<R>> Tree<R> of(R... elems) {
        Tree<R> tree = Leaf();
        for (R elem: elems) {
            tree = tree.add(elem);
        }
        return tree;
    }

    /**
     * Returns the static {@link Tree#TREE_LEAF} instance.
     * @param <R> The type that the tree encapsulates.
     */
    public static <R extends Comparable<R>> Tree<R> Leaf() {
        return (Tree<R>) TREE_LEAF;
    }

    /**
     * Creates a new {@link Tree}
     * @param node the value to store at the root
     * @param left the sub tree to store left of the node
     * @param right the sub tree to store right of the node
     * @param <R> The type that the tree encapsulates in each node.
     * @return the new {@link Tree} instance.
     * @throws NullPointerException if node, left or right are null.
     */
    public static <R extends Comparable<R>> Tree<R> Tree(R node, Tree<R> left, Tree<R> right) {
        return new Tree<>(Objects.requireNonNull(node), Objects.requireNonNull(left), Objects.requireNonNull(right));
    }

    /**
     * Private Tree constructor for creating {@link Tree} instances
     */
    private Tree(E node, Tree<E> left, Tree<E> right) {
        this.node = node;
        this.left = left;
        this.right = right;
    }

    /**
     * Checks if the Tree contains elem.
     * @param elem the element to check for in the Tree.
     * @return true if there is a node in the tree that equals elem.
     * @throws NullPointerException if elem is null.
     */
    public boolean contains(E elem) {
        Objects.requireNonNull(elem);
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> false),
            edge(() -> match(elem.compareTo(node)).of(
                with(¥EQ, () -> true),
                with(¥LT, () -> left.contains(elem)),
                with(¥GT, () -> right.contains(elem))
            ))
        );
    }

    /**
     * Checks if there are no nodes in this Tree.
     * @return true if it is a leaf node
     */
    public boolean isEmpty() {
        return TREE_LEAF.equals(this);
    }

    /**
     * Gets the root of this tree
     * @return {@link Option#nothing()} if this is a leaf, otherwise {@link Option} of the root value.
     */
    public Option<E> root() {
        return TREE_LEAF.equals(this) ? Option.nothing() : Option.of(node);
    }

    /**
     * Gets the height of this tree
     * @return int the height of this tree.
     */
    public int height() {
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> 0),
            edge(                            () -> 1 + Math.max(left.height(), right.height()))
        );
    }

    /**
     * Removes the largest element of this Tree.
     * @return a new Tree instance with the largest element removed
     */
    public Tree<E> deleteLargest() {
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> Leaf()),
            when(() -> isLeaf.test(right),   () -> left),
            edge(                            () -> Tree(node, left, right.deleteLargest()))
        );
    }

    /**
     * Finds the largest value of this Tree.
     * @return {@link Option#nothing()} if this is a leaf, otherwise {@link Option} of the largest value.
     */
    public Option<E> largest() {
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> Option.nothing()),
            when(() -> isLeaf.test(right),   () -> Option.of(node)),
            edge(                            () -> right.largest())
        );
    }

    /**
     * Adds the new element at the correct order in the tree.
     * @return a new Tree instance with the element added.
     * @throws NullPointerException if elem is null.
     */
    public Tree<E> add(E elem) {
        Objects.requireNonNull(elem);
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> Tree(elem, Leaf(), Leaf())),
            edge(() -> match(elem.compareTo(node)).of(
                with(¥LT, () -> Tree(node, left.add(elem), right)),
                with(¥GT, () -> Tree(node, left, right.add(elem))),
                with(¥EQ, () -> Tree(node, left, right))
            ))
        );
    }

    /**
     * Removes the element from the tree
     * @param elem The element to remove
     * @return A new Tree instance with the element removed.
     * @throws NullPointerException if elem is null.
     */
    public Tree<E> remove(E elem) {
        Objects.requireNonNull(elem);
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> Leaf()),
            edge(() -> match(elem.compareTo(node)).option(
                with(¥LT, () -> Tree(node, left.remove(elem), right)),
                with(¥GT, () -> Tree(node, left, right.remove(elem)))
            ).orElseGet(() -> guardUnsafe(
                when(() -> left == Leaf(), () -> right),
                edge(                      () -> Tree(left.largest().get(), left.deleteLargest(), right))
            )))
        );
    }

    /**
     * Checks if an object is the same instance, and if not, recursively checks each element for equality.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj == this ? true : Option.ofUnknown(obj)
                                          .cast(Tree.class)
                                          .map(this::eq)
                                          .orElse(false);
    }

    /**
     * Recursively analyses if the other tree is the same
     * @param tree The tree to check for equality
     * @return true if the trees match.
     */
    private boolean eq(Tree tree) {
        return guardUnsafe(
            when(() -> isLeaf.test(tree), () -> isLeaf.test(this)),
            edge(                         () -> node.equals(tree.node) && left.eq(tree.left) && right.eq(tree.right))
        );
    }

    @Override
    public int hashCode() {
        return hashCodeRec(1);
    }

    private int hashCodeRec(int hashCode) {
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> hashCode),
            edge(() -> {
                int hash = 17 * hashCode + node.hashCode();
                return hash + left.hashCodeRec(hash) + right.hashCodeRec(hash);
            })
        );
    }

    @Override
    public String toString() {
        return guardUnsafe(
            when(() -> isLeaf.test(this), () -> "Leaf"),
            edge(                            () -> "Node("+node+", "+left+", "+right+")")
        );
    }
}
