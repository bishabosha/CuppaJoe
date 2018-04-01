/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.immutable;

import io.cuppajoe.API;
import io.cuppajoe.Foldable;
import io.cuppajoe.Iterators;
import io.cuppajoe.Iterators.Lockable;
import io.cuppajoe.Unit;
import io.cuppajoe.control.Option;
import io.cuppajoe.functions.Func1;
import io.cuppajoe.functions.Func3;
import io.cuppajoe.tuples.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static io.cuppajoe.API.List;
import static io.cuppajoe.API.*;

/**
 * Immutable tree
 * @param <E> the type that the Tree contains
 */
public interface Tree<E extends Comparable<E>> {

    default E node() {
        throw new NoSuchElementException();
    }

    default Tree<E> left() {
        throw new NoSuchElementException();
    }

    default Tree<E> right() {
        throw new NoSuchElementException();
    }

    @SafeVarargs
    static <R extends Comparable<R>> Tree<R> of(R... elems) {
        Tree<R> tree = leaf();
        for (var elem: elems) {
            tree = tree.add(elem);
        }
        return tree;
    }

    /**
     * Returns the static {@link Leaf#INSTANCE} instance.
     * @param <R> The type that the tree encapsulates.
     */
    @SuppressWarnings("unchecked")
    static <R extends Comparable<R>> Tree<R> leaf() {
        return (Tree<R>) Leaf.INSTANCE;
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
        int comparison;
        return (elem == null || isEmpty())               ? false
            : (comparison = elem.compareTo(node())) == 0 ? true
            : comparison < 0                             ? left().contains(elem)
                                                         : right().contains(elem);
    }

    /**
     * Checks if there are no nodes in this Tree.
     * @return true if it is a leaf node
     */
    default boolean isEmpty() {
        return this == Leaf.INSTANCE;
    }

    /**
     * Gets the first in order element of this tree.
     * @throws java.util.NoSuchElementException if a leaf.
     */
    default E get() {
        var inOrder = inOrder().iterator();
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
        return isEmpty() ? 0
                         : 1 + Math.max(left().height(), right().height());
    }

    default int size() {
        return inOrder().foldLeft(0, (acc, x) -> acc + 1);
    }

    /**
     * Removes the largest element of this Tree.
     * @return a new Tree instance with the largest element removed
     */
    default Tree<E> deleteLargest() {
        return isEmpty()        ? leaf()
            : right().isEmpty() ? left()
                                : Node(node(), left(), right().deleteLargest());
    }

    /**
     * Finds the largest value of this Tree.
     * @return {@link API#None()} if this is a leaf, otherwise {@link Option} of the largest value.
     */
    default Option<E> largest() {
        return isEmpty()        ? None()
            : right().isEmpty() ? Some(node())
                                : right().largest();
    }

    /**
     * Adds the new element at the correct order in the tree.
     * @return a new Tree instance with the element added.
     * @throws NullPointerException if elem is null.
     */
    default Tree<E> add(E elem) {
        Objects.requireNonNull(elem);
        int comparison;
        return isEmpty()                                 ? Node(elem, leaf(), leaf())
            : (comparison = elem.compareTo(node())) == 0 ? this
            : comparison < 0                             ? Node(node(), left().add(elem), right())
                                                         : Node(node(), left(), right().add(elem));
    }

    /**
     * Removes the element from the tree
     * @param elem The element to remove
     * @return A new Tree instance with the element removed.
     */
    default Tree<E> remove(E elem) {
        int comparison;
        return Objects.isNull(elem)                     ? this
            : isEmpty()                                 ? leaf()
            : (comparison = elem.compareTo(node())) < 0 ? Node(node(), left().remove(elem), right())
            : comparison > 0                            ? Node(node(), left(), right().remove(elem))
            : left().isEmpty()                          ? right()
                                                        : Node(left().largest().get(), left().deleteLargest(), right());
    }

    default List<E> toList() {
        return inOrder().foldRight(List(), (List<E> xs, E x) -> xs.push(x));
    }

    default Foldable<E> inOrder() {
        return new Foldable<>() {

            @Override
            public <A> A foldRight(A accumulator, BiFunction<A, E, A> mapper) {
                return Foldable.foldOver(reverse(), accumulator, mapper);
            }

            private Iterable<E> reverse() {
                return () -> inOrderTraversal(Tree::left, Tree::right);
            }

            @Override
            public Iterator<E> iterator() {
                return inOrderTraversal(Tree::right, Tree::left);
            }

            /**
             * @param extractor should yield: [toReturn, nextTree, tail]
             * @param brancher  the function to choose which branch to go depth first on.
             * @return an Iterator that will do in order traversal
             */
            @SuppressWarnings("unchecked")
            private Iterator<E> inOrderTraversal(Func1<Tree<E>, Tree<E>> extractor, UnaryOperator<Tree<E>> brancher) {
                return new Lockable<>() {

                    private List<Tree<E>> stack = List.empty();
                    private Tree<E> current = Tree.this;
                    private E toReturn;

                    @Override
                    public boolean hasNextSupplier() {
                        while (!current.isEmpty()) {
                            stack = stack.push(current);
                            current = brancher.apply(current);
                        }
                        return stack.pop()
                                    .flatMap(this::match)
                                    .containsValue();
                    }

                    private Option<Unit> match(Product2<Tree<E>, List<Tree<E>>> tuple) {
                        return tuple.compose(this::extract);
                    }

                    private Option<Unit> extract(Tree<E> tree, List<Tree<E>> list) {
                        return tree.isEmpty() ? None() : Some(processPopped(tree.node(), extractor.apply(tree), list));
                    }

                    private Unit processPopped(E head, Tree<E> nextTree, List<Tree<E>> tail) {
                        toReturn = head;
                        current = nextTree;
                        stack = tail;
                        return Unit.INSTANCE;
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
        return () -> new Lockable<>() {

            private List<Tree<E>> stack = List.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return stack.pop()
                            .map(this::match)
                            .containsValue();
            }

            private Option<Unit> match(Product2<Tree<E>, List<Tree<E>>> tuple) {
                return tuple.compose(this::extract);
            }

            private Option<Unit> extract(Tree<E> tree, List<Tree<E>> list) {
                return tree.isEmpty() ? None() : Some(processPopped(tree.node(), tree.left(), tree.right(), list));
            }

            private Unit processPopped(E node, Tree<E> left, Tree<E> right, List<Tree<E>> tail) {
                toReturn = node;
                if (!right.isEmpty()) {
                    tail = tail.push(right);
                }
                if (!left.isEmpty()) {
                    tail = tail.push(left);
                }
                stack = tail;
                return Unit.INSTANCE;
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    default Iterable<E> levelOrder() {
        return () -> new Lockable<>() {

            private Queue<Tree<E>> queue = Queue.of(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return queue.dequeue()
                            .map(this::match)
                            .containsValue();
            }

            private Option<Unit> match(Product2<Tree<E>, Queue<Tree<E>>> tuple) {
                return tuple.compose(this::extract);
            }

            private Option<Unit> extract(Tree<E> tree, Queue<Tree<E>> list) {
                return tree.isEmpty() ? None() : Some(processPopped(tree.node(), tree.left(), tree.right(), list));
            }

            private Unit processPopped(E node, Tree<E> left, Tree<E> right, Queue<Tree<E>> remaining) {
                toReturn = node;
                if (!left.isEmpty()) {
                    remaining = remaining.enqueue(left);
                }
                if (!right.isEmpty()) {
                    remaining = remaining.enqueue(right);
                }
                queue = remaining;
                return Unit.INSTANCE;
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    @SuppressWarnings("unchecked")
    default Iterable<E> postOrder() {
        return () -> new Lockable<>() {

            private List<Object> stack = List.of(Tree.this);
            private Option<E> toReturn;

            @Override
            public boolean hasNextSupplier() {
                final Product2<Option<E>, List<Object>> nextItem;
                nextItem = stack.nextItem((x, xs) -> {
                    if (x instanceof Node) {
                        var tree = (Node<E>) x;
                        xs = xs.push(tree.node);
                        if (!tree.right.isEmpty()) {
                            xs = xs.push(tree.right);
                        }
                        if (!tree.left.isEmpty()) {
                            xs = xs.push(tree.left);
                        }
                        return Some(Tuple(None(), xs));
                    } else if (x == leaf()) {
                        return None();
                    } else {
                        return Some(Tuple(Some((E)x), xs));
                    }
                });
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

    enum Leaf implements Tree<Unit>, Unapply0 {

        INSTANCE;

        @Override
        public int height() {
            return 0;
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

        private Node(E node, Tree<E> left, Tree<E> right) {
            this.node = node;
            this.left = left;
            this.right = right;
        }

        public static <O extends Comparable<O>> Node<O> of(O node, Tree<O> left, Tree<O> right) {
            return new Node<>(node, left, right);
        }

        static <R extends Comparable<R>> Apply3<R, Tree<R>, Tree<R>, Tree<R>> Applied() {
            return Func3.<R, Tree<R>, Tree<R>, Tree<R>>of(Tree::Node).tupled()::apply;
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
                    for (var x: inOrder()) {
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
            return Iterators.toString('[', ']', inOrder().iterator());
        }
    }
}
