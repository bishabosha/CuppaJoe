/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.immutable;

import com.github.bishabosha.cuppajoe.annotation.NonNull;
import com.github.bishabosha.cuppajoe.control.Either;
import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.higher.foldable.Foldable;
import com.github.bishabosha.cuppajoe.higher.functions.Func1;
import com.github.bishabosha.cuppajoe.tuples.*;
import com.github.bishabosha.cuppajoe.util.Iterators;
import com.github.bishabosha.cuppajoe.util.Iterators.IdempotentIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.github.bishabosha.cuppajoe.API.*;

/**
 * Immutable tree
 *
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
    static <R extends Comparable<R>> Tree<R> ofAll(R... elems) {
        Tree<R> tree = Leaf();
        if (elems == null) {
            return tree;
        }
        for (var elem : elems) {
            tree = tree.add(elem);
        }
        return tree;
    }

    /**
     * Returns the static {@link Leaf#INSTANCE} instance.
     *
     * @param <R> The type that the tree encapsulates.
     */
    @SuppressWarnings("unchecked")
    static <R extends Comparable<R>> Tree<R> Leaf() {
        return (Tree<R>) Leaf.INSTANCE;
    }

    /**
     * Creates a new {@link Tree}
     *
     * @param node  the value to store at the root
     * @param left  the sub tree to store left of the node
     * @param right the sub tree to store right of the node
     * @param <R>   The type that the tree encapsulates in each node.
     * @return the new {@link Tree} instance.
     * @throws NullPointerException if node, left or right are null.
     */
    static <R extends Comparable<R>> Tree<R> Node(@NonNull R node, @NonNull Tree<R> left, @NonNull Tree<R> right) {
        return Node.of(node, left, right);
    }

    /**
     * Checks if the Tree contains elem.
     *
     * @param elem the element to check for in the Tree.
     * @return true if there is a node in the tree that equals elem.
     */
    default boolean contains(E elem) {
        int comparison;
        return elem != null && !isEmpty()
            && ((comparison = elem.compareTo(node())) == 0
                    || comparison < 0 ? left().contains(elem) : right().contains(elem));
    }

    /**
     * Checks if there are no nodes in this Tree.
     *
     * @return true if it is a leaf node
     */
    default boolean isEmpty() {
        return this == Leaf.INSTANCE;
    }

    /**
     * Gets the first in order element of this tree.
     *
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
     *
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
     *
     * @return a new Tree instance with the largest element removed
     */
    default Tree<E> deleteLargest() {
        return isEmpty() ? Leaf()
                : right().isEmpty() ? left()
                : Node(node(), left(), right().deleteLargest());
    }

    /**
     * Finds the largest value of this Tree.
     *
     * @return {@link Option.None} if this is a leaf, otherwise {@link Option} of the largest value.
     */
    default Option<E> largest() {
        return isEmpty() ? None()
                : right().isEmpty() ? Some(node())
                : right().largest();
    }

    /**
     * Adds the new element at the correct order in the tree.
     *
     * @return a new Tree instance with the element added.
     * @throws NullPointerException if elem is null.
     */
    default Tree<E> add(@NonNull E elem) {
        Objects.requireNonNull(elem, "elem");
        int comparison;
        return isEmpty() ? Node(elem, Leaf(), Leaf())
                : (comparison = elem.compareTo(node())) == 0 ? this
                : comparison < 0 ? Node(node(), left().add(elem), right())
                : Node(node(), left(), right().add(elem));
    }

    /**
     * Removes the element from the tree
     *
     * @param elem The element to remove
     * @return A new Tree instance with the element removed.
     */
    default Tree<E> remove(E elem) {
        int comparison;
        return Objects.isNull(elem) ? this
                : isEmpty() ? Leaf()
                : (comparison = elem.compareTo(node())) < 0 ? Node(node(), left().remove(elem), right())
                : comparison > 0 ? Node(node(), left(), right().remove(elem))
                : left().isEmpty() ? right()
                : Node(left().largest().get(), left().deleteLargest(), right());
    }

    default List<E> toList() {
        return inOrder().foldRight(API.List(), List::push);
    }

    default Foldable<E> inOrder() {
        return new Foldable<>() {

            @Override
            public <A> A foldRight(A accumulator, @NonNull BiFunction<A, E, A> mapper) {
                return Foldable.foldOver(reverse(), accumulator, mapper);
            }

            private Iterable<E> reverse() {
                return () -> inOrderTraversal(Tree::right, Tree::left);
            }

            @Override
            public Iterator<E> iterator() {
                return inOrderTraversal(Tree::left, Tree::right);
            }

            /**
             * @param extractor should yield: [toReturn, nextTree, tail]
             * @param brancher  the function to choose which branch to go depth first on.
             * @return an Iterator that will do in order traversal
             */
            @SuppressWarnings("unchecked")
            private Iterator<E> inOrderTraversal(UnaryOperator<Tree<E>> brancher, Func1<Tree<E>, Tree<E>> extractor) {
                return new IdempotentIterator<>() {

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

                    private Option<Unit> match(Tuple2<Tree<E>, List<Tree<E>>> tuple) {
                        return tuple.compose(this::extract);
                    }

                    private Option<Unit> extract(Tree<E> tree, List<Tree<E>> list) {
                        return tree.isEmpty() ? None()
                                : Some(processPopped(tree.node(), extractor.apply(tree), list));
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
        return () -> new IdempotentIterator<>() {

            private List<Tree<E>> stack = List.singleton(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return stack.nextItem(this::stackAlgorithm)
                        .map(this::foundNext)
                        .containsValue();
            }

            private Either<List<Tree<E>>, Tuple2<E, List<Tree<E>>>> stackAlgorithm(Tree<E> head, List<Tree<E>> tail) {
                return head.isEmpty() ? Left(API.List())
                        : Right(Tuple(head.node(), processNode(head.left(), head.right(), tail)));
            }

            private List<Tree<E>> processNode(Tree<E> left, Tree<E> right, List<Tree<E>> list) {
                var rightEmpty = right.isEmpty();
                var leftEmpty = left.isEmpty();
                return leftEmpty ? rightEmpty
                          ? list
                          : list.push(right)
                        : rightEmpty
                          ? list.push(left)
                          : list.push(right).push(left);
            }

            private Unit foundNext(Tuple2<E, List<Tree<E>>> headTail) {
                return headTail.compose((head, tail) -> {
                    stack = tail;
                    toReturn = head;
                    return Unit.INSTANCE;
                });
            }

            @Override
            public E nextSupplier() {
                return toReturn;
            }
        };
    }

    default Iterable<E> levelOrder() {
        return () -> new IdempotentIterator<>() {

            private Queue<Tree<E>> queue = Queue.singleton(Tree.this);
            private E toReturn;

            @Override
            public boolean hasNextSupplier() {
                return queue.dequeue()
                        .flatMap(this::match)
                        .containsValue();
            }

            private Option<Unit> match(Tuple2<Tree<E>, Queue<Tree<E>>> tuple) {
                return tuple.compose(this::extract);
            }

            private Option<Unit> extract(Tree<E> tree, Queue<Tree<E>> list) {
                return tree.isEmpty() ? None()
                        : Some(processTree(tree.node(), tree.left(), tree.right(), list));
            }

            private Unit processTree(E node, Tree<E> left, Tree<E> right, Queue<Tree<E>> remaining) {
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
        return () -> new IdempotentIterator<>() {

            private List<Object> stack = API.List(Tree.this);
            private E current;

            @Override
            public boolean hasNextSupplier() {
                return stack.nextItem(this::stackAlgorithm)
                        .map(this::foundNext)
                        .containsValue();
            }

            private Either<List<Object>, Tuple2<E, List<Object>>> stackAlgorithm(Object head, List<Object> tail) {
                return head instanceof Node ? Left(processNode((Node<E>) head, tail))
                        : head == Leaf() ? Left(API.List())
                        : Right(Tuple((E) head, tail));
            }

            private List<Object> processNode(Node<E> node, List<Object> list) {
                var rightEmpty = node.right.isEmpty();
                var leftEmpty = node.left.isEmpty();
                return leftEmpty ? rightEmpty
                          ? list.push(node.node)
                          : list.push(node.node).push(node.right)
                        : rightEmpty
                          ? list.push(node.node).push(node.left)
                          : list.push(node.node).push(node.right).push(node.left);
            }

            private Unit foundNext(Tuple2<E, List<Object>> tuple) {
                return tuple.compose((elem, xs) -> {
                    stack = xs;
                    current = elem;
                    return Unit.INSTANCE;
                });
            }

            @Override
            public E nextSupplier() {
                return current;
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

        public static <O extends Comparable<O>> Node<O> of(@NonNull O node, @NonNull Tree<O> left, @NonNull Tree<O> right) {
            Objects.requireNonNull(node, "node");
            Objects.requireNonNull(left, "left");
            Objects.requireNonNull(right, "right");
            return new Node<>(node, left, right);
        }

        @Override
        public Tuple3<E, Tree<E>, Tree<E>> unapply() {
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
            return inOrder().foldLeft(1, (hash, x) -> 31 * hash + (x == null ? 0 : Objects.hashCode(x)));
        }

        /**
         * Checks if an object is the same instance, and if not, checks for the same elements in order.
         */
        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof Node && Iterators.equals(inOrder().iterator(), ((Node) obj).inOrder().iterator());
        }

        @Override
        public String toString() {
            return Iterators.toString('[', ']', inOrder().iterator());
        }
    }
}
