/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.immutable;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.functional.*;
import com.bishabosha.caffeine.functional.tuples.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static com.bishabosha.caffeine.functional.API.*;
import static com.bishabosha.caffeine.functional.API.Tuple;
import static com.bishabosha.caffeine.functional.Case.*;
import static com.bishabosha.caffeine.functional.Matcher.guardUnsafe;
import static com.bishabosha.caffeine.functional.Matcher.match;
import static com.bishabosha.caffeine.functional.Option.Some;
import static com.bishabosha.caffeine.functional.Pattern.*;
import static com.bishabosha.caffeine.functional.PatternFactory.patternFor;
import static com.bishabosha.caffeine.functional.tuples.Tuple2.Tuple;

/**
 * Immutable tree
 * @param <E> the type that the Tree contains
 */
public class Tree<E extends Comparable<E>> {

    /**
     * Singleton instance of the leaf node
     */
    private static final Tree<?> TREE_LEAF = new Tree<>(null, null, null);


    /**
     * This will check {@link Tree#node} {@link Tree#left} and {@link Tree#right} for <b>null</b> without binding.
     */
    public static final Pattern Leaf() {
        return x -> Option.of(x)
                          .cast(Tree.class)
                          .filter(Tree::isLeaf)
                          .flatMap(t -> Pattern.PASS);
    }

    /**
     * For use after {@link Tree#Leaf()}. This will apply patterns to {@link Tree#node} {@link Tree#left} and {@link Tree#right}
     * @param node The pattern to check the node
     * @param left The pattern to check the left sub tree
     * @param right The pattern to check the right sub tree
     * @return <b>Option&lt;PatternResult&gt;</b> if all patterns pass, otherwise {@link Option#nothing()}
     */
    public static Pattern Node(Pattern node, Pattern left, Pattern right) {
        return patternFor(Tree.class).testThree(
            Tuple2.of(node, x -> x.node),
            Tuple2.of(left, x -> x.left),
            Tuple2.of(right, x -> x.right)
        );
    }

    private E node;
    private Tree<E> left;
    private Tree<E> right;

    public static <R extends Comparable<R>> Tree<R> of(R... elems) {
        Tree<R> tree = leaf();
        for (R elem: elems) {
            tree = tree.add(elem);
        }
        return tree;
    }

    /**
     * Returns the static {@link Tree#TREE_LEAF} instance.
     * @param <R> The type that the tree encapsulates.
     */
    public static <R extends Comparable<R>> Tree<R> leaf() {
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
    public static <R extends Comparable<R>> Tree<R> Node(R node, Tree<R> left, Tree<R> right) {
        return new Tree<>(Objects.requireNonNull(node), Objects.requireNonNull(left), Objects.requireNonNull(right));
    }

    public Tree<E> left() {
        return left;
    }

    public Tree<E> right() {
        return right;
    }

    public E node() {
        return node;
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
        if (Objects.isNull(elem)) {
            return false;
        }
        return guardUnsafe(
            when(this::isLeaf, () -> false),
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
    public boolean isLeaf() {
        return this == TREE_LEAF || Library.allEqual(null, node, left, right);
    }

    /**
     * Gets the root of this tree
     * @return {@link Option#nothing()} if this is a leaf, otherwise {@link Option} of the root value.
     */
    public Option<E> root() {
        return isLeaf() ? Option.nothing() : Option.of(node);
    }

    /**
     * Gets the height of this tree
     * @return int the height of this tree.
     */
    public int height() {
        return guardUnsafe(
            when(this::isLeaf, () -> 0),
            edge(              () -> 1 + Math.max(left.height(), right.height()))
        );
    }

    public int size() {
        return inOrder().foldLeft(0, (x, y) -> y + 1);
    }

    /**
     * Removes the largest element of this Tree.
     * @return a new Tree instance with the largest element removed
     */
    public Tree<E> deleteLargest() {
        return guardUnsafe(
            when(this::isLeaf,  () -> leaf()),
            when(right::isLeaf, () -> left),
            edge(               () -> Node(node, left, right.deleteLargest()))
        );
    }

    /**
     * Finds the largest value of this Tree.
     * @return {@link Option#nothing()} if this is a leaf, otherwise {@link Option} of the largest value.
     */
    public Option<E> largest() {
        return guardUnsafe(
            when(this::isLeaf,  () -> Option.nothing()),
            when(right::isLeaf, () -> Option.of(node)),
            edge(               () -> right.largest())
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
            when(this::isLeaf, () -> Node(elem, leaf(), leaf())),
            edge(() -> match(elem.compareTo(node)).of(
                with(¥LT, () -> Node(node, left.add(elem), right)),
                with(¥GT, () -> Node(node, left, right.add(elem))),
                with(¥EQ, () -> Node(node, left, right))
            ))
        );
    }

    /**
     * Removes the element from the tree
     * @param elem The element to remove
     * @return A new Tree instance with the element removed.
     */
    public Tree<E> remove(E elem) {
        return Objects.isNull(elem) ? this : guardUnsafe(
            when(this::isLeaf, Tree::leaf),
            edge(() -> match(elem.compareTo(node)).of(
                with(¥LT, () -> Node(node, left.remove(elem), right)),
                with(¥GT, () -> Node(node, left, right.remove(elem))),
                with(¥EQ, () -> guardUnsafe(
                    when(() -> left == leaf(), () -> right),
                    edge(                      () -> Node(left.largest().get(), left.deleteLargest(), right))
                ))
            ))
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
            when(this::isLeaf, tree::isLeaf),
            edge(() -> node.equals(tree.node) && left.eq(tree.left) && right.eq(tree.right))
        );
    }

    public Cons<E> toCons() {
        return inOrder().foldRight(Cons.empty(), (x, xs) -> xs.push(x));
    }

    public List<E> toJavaList() {
        return inOrder().foldLeft(new ArrayList<>(), (x, xs) -> {
            xs.add(x);
            return xs;
        });
    }

    public Foldable<E> inOrder() {
        return new Foldable<E>() {

            @Override
            public Iterator<E> iterator() {
                return inOrderTraversal(Tuple(Some(Node($n, ¥_, $r)), $xs), Tree::left);
            }

            @Override
            public Iterable<E> reverse() {
                return () -> inOrderTraversal(Tuple(Some(Node($n, $l, ¥_)), $xs), Tree::right);
            }

            /**
             * @param extractor should yield: [toReturn, nextTree, tail]
             * @param brancher the function to choose which branch to go depth first on.
             * @return an Iterator that will do in order traversal
             */
            @SuppressWarnings("unchecked")
            private Iterator<E> inOrderTraversal(Pattern extractor, UnaryOperator<Tree<E>> brancher) {
                return new Iterables.Lockable<E>() {

                    private Cons<Tree<E>> stack = Cons.empty();
                    private Tree<E> current = Tree.this;
                    private E toReturn;

                    @Override
                    public boolean hasNextSupplier() {
                        while (!current.isLeaf()) {
                            stack = stack.push(current);
                            current = brancher.apply(current);
                        }
                        return stack.pop(with(extractor, this::processPopped))
                                    .orElse(false);
                    }

                    private boolean processPopped(E head, Tree<E> nextTree, Cons<Tree<E>> tail) {
                        toReturn = head;
                        current = nextTree;
                        stack = tail;
                        return true;
                    }

                    @Override
                    public E nextSupplier() {
                        return toReturn;
                    }
                };
            }
        };
    }

    public Iterable<E> preOrder() {
        return () -> new Iterables.Lockable<E>() {

            private Cons<Tree<E>> stack = Cons.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return stack.pop(with(Tuple(Some(Node($n, $l, $r)), $xs), this::processPopped))
                            .orElse(false);
            }

            private boolean processPopped(E node, Tree<E> left, Tree<E> right, Cons<Tree<E>> tail) {
                toReturn = node;
                if (!right.isLeaf()) {
                    tail = tail.push(right);
                }
                if (!left.isLeaf()) {
                    tail = tail.push(left);
                }
                stack = tail;
                return true;
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public Iterable<E> postOrder() {
        return () -> new Iterables.Lockable<E>() {

            private Cons<Object> stack = Cons.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                final Tuple2<Option<E>, Cons<Object>> nextItem;
                nextItem = stack.nextItem((x, xs) -> match(x).of(
                    with(Some(Leaf()), () -> Tuple(Left(false), xs)),
                    with(Some(Node($n, $l, $r)), (E $n, Tree<E> $l, Tree<E> $r) -> {
                        Cons<Object> zs = xs;
                        zs = zs.push($n);
                        if (!$r.isLeaf()) {
                            zs = zs.push($r);
                        }
                        if (!$l.isLeaf()) {
                            zs = zs.push($l);
                        }
                        return Tuple(Left(true), zs);
                    }),
                    with(Some($x), (E $x) -> Tuple(Right($x), xs))
                ));
                stack = nextItem.$2();
                toReturn = nextItem.$1().orElse(null);
                return nextItem.$1().isSome();
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    @Override
    public int hashCode() {
        return hashCodeRec(1);
    }

    private int hashCodeRec(int hashCode) {
        return guardUnsafe(
            when(this::isLeaf, () -> hashCode),
            edge(() -> {
                int hash = 17 * hashCode + node.hashCode();
                return hash + left.hashCodeRec(hash) + right.hashCodeRec(hash);
            })
        );
    }

    @Override
    public String toString() {
        return Iterables.toString('[', ']', inOrder().iterator());
    }
}
