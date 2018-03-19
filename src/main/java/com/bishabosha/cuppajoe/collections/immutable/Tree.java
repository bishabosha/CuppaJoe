/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.API;
import com.bishabosha.cuppajoe.Foldable;
import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func3;
import com.bishabosha.cuppajoe.patterns.Pattern;
import com.bishabosha.cuppajoe.patterns.PatternFactory;
import com.bishabosha.cuppajoe.tuples.Apply3;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Product3;
import com.bishabosha.cuppajoe.tuples.Unapply3;
import org.jetbrains.annotations.Contract;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.bishabosha.cuppajoe.API.List;
import static com.bishabosha.cuppajoe.API.*;
import static com.bishabosha.cuppajoe.collections.immutable.Tree.Leaf.¥Leaf;
import static com.bishabosha.cuppajoe.collections.immutable.Tree.Node.$Node;
import static com.bishabosha.cuppajoe.patterns.Case.*;
import static com.bishabosha.cuppajoe.patterns.Matcher.guardUnsafe;
import static com.bishabosha.cuppajoe.patterns.Pattern.*;
import static com.bishabosha.cuppajoe.tuples.Tuple2.$Tuple2;

/**
 * Immutable tree
 * @param <E> the type that the Tree contains
 */
public interface Tree<E extends Comparable<E>> {

    E node();
    Tree<E> left();
    Tree<E> right();

    @SafeVarargs
    static <R extends Comparable<R>> Tree<R> of(R... elems) {
        Tree<R> tree = Leaf.getInstance();
        for (R elem: elems) {
            tree = tree.add(elem);
        }
        return tree;
    }

    /**
     * Returns the static {@link Leaf#LEAF} instance.
     * @param <R> The type that the tree encapsulates.
     */
    static <R extends Comparable<R>> Tree<R> leaf() {
        return Leaf.getInstance();
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
    static <R extends Comparable<R>> Tree<R> Node(R node, Tree<R> left, Tree<R> right) {
        return Node.of(
            Objects.requireNonNull(node),
            Objects.requireNonNull(left),
            Objects.requireNonNull(right)
        );
    }

    /**
     * Checks if the Tree contains elem.
     * @param elem the element to check for in the Tree.
     * @return true if there is a node in the tree that equals elem.
     * @throws NullPointerException if elem is null.
     */
    default boolean contains(E elem) {
        if (Objects.isNull(elem)) {
            return false;
        }
        return guardUnsafe(
            when(this::isEmpty, () -> false),
            edge(() -> Match(elem.compareTo(node())).of(
                with(¥EQ, () -> true),
                with(¥LT, () -> left().contains(elem)),
                with(¥GT, () -> right().contains(elem))
            ))
        );
    }

    /**
     * Checks if there are no nodes in this Tree.
     * @return true if it is a leaf node
     */
    default boolean isEmpty() {
        return this == Leaf.LEAF;
    }

    /**
     * Gets the first in order element of this tree.
     * @throws java.util.NoSuchElementException if a leaf.
     */
    default E get() {
        Iterator<E> inOrder = inOrder().iterator();
        if (!inOrder.hasNext()) {
            throw new NoSuchElementException();
        }
        return inOrder.next();
    }

    /**
     * Gets the height of this tree
     * @return int the height of this tree.
     */
    default int height() {
        return guardUnsafe(
            when(this::isEmpty, () -> 0),
            edge(               () -> 1 + Math.max(left().height(), right().height()))
        );
    }

    default int size() {
        return inOrder().foldLeft(0, (acc, x) -> acc + 1);
    }

    /**
     * Removes the largest element of this Tree.
     * @return a new Tree instance with the largest element removed
     */
    default Tree<E> deleteLargest() {
        return guardUnsafe(
            when(this::isEmpty,    () -> leaf()),
            when(right()::isEmpty, () -> left()),
            edge(                  () -> Node(node(), left(), right().deleteLargest()))
        );
    }

    /**
     * Finds the largest value of this Tree.
     * @return {@link API#Nothing()} if this is a leaf, otherwise {@link Option} of the largest value.
     */
    default Option<E> largest() {
        return guardUnsafe(
            when(this::isEmpty,    () -> Nothing()),
            when(right()::isEmpty, () -> Some(node())),
            edge(                  () -> right().largest())
        );
    }

    /**
     * Adds the new element at the correct order in the tree.
     * @return a new Tree instance with the element added.
     * @throws NullPointerException if elem is null.
     */
    default Tree<E> add(E elem) {
        Objects.requireNonNull(elem);
        return guardUnsafe(
            when(this::isEmpty, () -> Node(elem, leaf(), leaf())),
            edge(() -> Match(elem.compareTo(node())).of(
                with(¥LT, () -> Node(node(), left().add(elem), right())),
                with(¥GT, () -> Node(node(), left(), right().add(elem))),
                with(¥EQ, () -> Node(node(), left(), right()))
            ))
        );
    }

    /**
     * Removes the element from the tree
     * @param elem The element to remove
     * @return A new Tree instance with the element removed.
     */
    default Tree<E> remove(E elem) {
        return Objects.isNull(elem) ? this : guardUnsafe(
            when(this::isEmpty, Tree::leaf),
            edge(() -> Match(elem.compareTo(node())).of(
                with(¥LT, () -> Node(node(), left().remove(elem), right())),
                with(¥GT, () -> Node(node(), left(), right().remove(elem))),
                with(¥EQ, () -> guardUnsafe(
                    when(left()::isEmpty, () -> right()),
                    edge(                 () -> Node(left().largest().get(), left().deleteLargest(), right()))
                ))
            ))
        );
    }

    default List<E> toList() {
        return inOrder().foldRight(List(), (xs, x) -> xs.push(x));
    }

    default Foldable<E> inOrder() {
        return new Foldable<>() {

            @Override
            public <A> A foldRight(A accumulator, BiFunction<A, E, A> mapper) {
                return Foldable.foldOver(reverse(), accumulator, mapper);
            }

            private Iterable<E> reverse() {
                return () -> inOrderTraversal($Tuple2($Node($n, $l, ¥_), $xs), Tree::right);
            }

            @Override
            public Iterator<E> iterator() {
                return inOrderTraversal($Tuple2($Node($n, ¥_, $r), $xs), Tree::left);
            }

            /**
             * @param extractor should yield: [toReturn, nextTree, tail]
             * @param brancher  the function to choose which branch to go depth first on.
             * @return an Iterator that will do in order traversal
             */
            @SuppressWarnings("unchecked")
            private Iterator<E> inOrderTraversal(Pattern extractor, UnaryOperator<Tree<E>> brancher) {
                return new Iterables.Lockable<>() {

                    private List<Tree<E>> stack = List.empty();
                    private Tree<E> current = Tree.this;
                    private E toReturn;

                    @Override
                    public boolean hasNextSupplier() {
                        while (!current.isEmpty()) {
                            stack = stack.push(current);
                            current = brancher.apply(current);
                        }
                        return stack.pop(with(extractor, this::processPopped))
                                    .orElse(false);
                    }

                    private boolean processPopped(E head, Tree<E> nextTree, List<Tree<E>> tail) {
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

    default Iterable<E> preOrder() {
        return () -> new Iterables.Lockable<>() {

            private List<Tree<E>> stack = List.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return stack.pop(with($Tuple2($Node($n, $l, $r), $xs), this::processPopped))
                            .orElse(false);
            }

            private boolean processPopped(E node, Tree<E> left, Tree<E> right, List<Tree<E>> tail) {
                toReturn = node;
                if (!right.isEmpty()) {
                    tail = tail.push(right);
                }
                if (!left.isEmpty()) {
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

    default Iterable<E> levelOrder() {
        return () -> new Iterables.Lockable<>() {

            private Queue<Tree<E>> queue = Queue.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return queue.dequeue(with($Tuple2($Node($n, $l, $r), $xs), this::processPopped))
                            .orElse(false);
            }

            private boolean processPopped(E node, Tree<E> left, Tree<E> right, Queue<Tree<E>> remaining) {
                toReturn = node;
                if (!left.isEmpty()) {
                    remaining = remaining.enqueue(left);
                }
                if (!right.isEmpty()) {
                    remaining = remaining.enqueue(right);
                }
                queue = remaining;
                return true;
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    @SuppressWarnings("unchecked")
    default Iterable<E> postOrder() {
        return () -> new Iterables.Lockable<>() {

            private List<Object> stack = List.of(Tree.this);
            private Option<E> toReturn;

            @Override
            public boolean hasNextSupplier() {
                final Product2<Option<E>, List<Object>> nextItem;
                nextItem = stack.nextItem((x, xs) -> Match(x).of(
                    with(¥Leaf(), () -> Nothing()),
                    with($Node($n, $l, $r), (E $n, Tree<E> $l, Tree<E> $r) -> {
                        List<Object> zs = xs;
                        zs = zs.push($n);
                        if (!$r.isEmpty()) {
                            zs = zs.push($r);
                        }
                        if (!$l.isEmpty()) {
                            zs = zs.push($l);
                        }
                        return Some(Tuple(Nothing(), zs));
                    }),
                    with($x, (E $x) -> Some(Tuple(Some($x), xs)))
                ));
                stack = nextItem.$2();
                toReturn = nextItem.$1();
                return !nextItem.$1().isEmpty();
            }

            @Override
            public E nextSupplier() {
                return toReturn.get();
            }
        };
    }

    class Leaf<E extends Comparable<E>> implements Tree<E> {

        /**
         * Singleton instance of the leaf node
         */
        private static final Leaf<?> LEAF = new Leaf<>();

        @Contract(pure = true)
        @SuppressWarnings("unchecked")
        public static <O extends Comparable<O>> Leaf<O> getInstance() {
            return (Leaf<O>) LEAF;
        }

        public static Pattern ¥Leaf() {
            return x -> x == Leaf.LEAF ? PASS : FAIL;
        }

        private Leaf() {
        }

        @Override
        public E node() {
            throw new NoSuchElementException();
        }

        @Override
        public Tree<E> left() {
            throw new NoSuchElementException();
        }

        @Override
        public Tree<E> right() {
            throw new NoSuchElementException();
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == LEAF;
        }

        @Override
        public String toString() {
            return "[]";
        }
    }

    class Node<E extends Comparable<E>> implements Tree<E>, Unapply3<E, Tree<E>, Tree<E>> {

        private final E node;
        private final Tree<E> left;
        private final Tree<E> right;

        private static final Func3<Pattern, Pattern, Pattern, Pattern> PATTERN = PatternFactory.gen3(Node.class);

        /**
         * @param node The pattern to check the node
         * @param left The pattern to check the left sub tree
         * @param right The pattern to check the right sub tree
         * @return <b>Option&lt;Result&gt;</b> if all patterns pass, otherwise {@link API#Nothing()}
         */
        public static Pattern $Node(Pattern node, Pattern left, Pattern right) {
            return PATTERN.apply(node, left, right);
        }

        private Node(E node, Tree<E> left, Tree<E> right) {
            this.node = node;
            this.left = left;
            this.right = right;
        }

        public static <O extends Comparable<O>> Node<O> of(O node, Tree<O> left, Tree<O> right) {
            return new Node<>(node, left, right);
        }

        static <R extends Comparable<R>> Apply3<R, Tree<R>, Tree<R>, Tree<R>> Applied() {
            return Func3.<R, Tree<R>, Tree<R>, Tree<R>>of(Tree::Node).applied();
        }

        @Override
        public Product3<E, Tree<E>, Tree<E>> unapply() {
            return Tuple(node(), left(), right());
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

        @Override
        public int hashCode() {
            return inOrder().foldLeft(1, (hash, x) -> 31*hash + (x == null ? 0 : Objects.hashCode(x)));
        }

        /**
         * Checks if an object is the same instance, and if not, recursively checks each element for equality.
         * @param obj
         * @return
         */
        @Override
        public boolean equals(Object obj) {
            return obj == this || Option.of(obj)
                .cast(Node.class)
                .map(n -> {
                    Iterator<?> elems = n.inOrder().iterator();
                    for (E x: inOrder()) {
                        if (!(elems.hasNext() && Objects.equals(x, elems.next()))) {
                            return false;
                        }
                    }
                    return !elems.hasNext();
                })
                .orElse(false);
        }

        @Override
        public String toString() {
            return Iterables.toString('[', ']', inOrder().iterator());
        }
    }
}
