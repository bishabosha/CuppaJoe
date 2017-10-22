package com.bishabosha.cuppajoe.collections.immutable;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.Library;
import com.bishabosha.cuppajoe.control.Option;
import com.bishabosha.cuppajoe.functions.Func2;
import com.bishabosha.cuppajoe.tuples.Product2;
import com.bishabosha.cuppajoe.tuples.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.bishabosha.cuppajoe.API.*;

public interface BaseTree<E> extends Iterable<E> {

    @SafeVarargs
    static <E> BaseTree<E> compose(BaseTree<E>... trees) {
        return trees == null
            ? new Leaf<>(null)
            : new Node<>(Array.of(trees));
    }

    static <E> BaseTree<E> compose(BaseTree<E> tree) {
        return new Node<>(Array.of(tree));
    }

    @SafeVarargs
    static <E> BaseTree<E> of(E... values) {
        return values == null
            ? new Leaf<>(null)
            : values.length == 0
                ? empty()
                : new Node<>(Array.of(values).map(Leaf::new));
    }

    static <E> BaseTree<E> of(E value) {
        return new Leaf<>(value);
    }

    @SuppressWarnings("unchecked")
    static <E> BaseTree<E> empty() {
        return (BaseTree<E>) Node.EMPTY;
    }

    boolean isEmpty();

    boolean isLeaf();

    E get();

    Array<BaseTree<E>> branches();

    class Node<E> implements BaseTree<E> {

        private static final BaseTree<?> EMPTY = new Node<>(Array.empty());

        Array<BaseTree<E>> branches;

        private Node(Array<BaseTree<E>> branches) {
            this.branches = branches;
        }

        @Override
        public boolean isEmpty() {
            return branches.isEmpty();
        }

        public boolean isLeaf() {
            return false;
        }

        @Override
        public E get() {
            BaseTree<E> tree = this;
            while (!tree.isEmpty()) {
                if (tree.isLeaf()) {
                    return tree.get();
                } else {
                    tree = tree.branches().head();
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public Array<BaseTree<E>> branches() {
            return branches;
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return new Iterables.Lockable<>() {
                private List<Iterator<BaseTree<E>>> stack = isEmpty() ? List() : List(Node.this.branches().iterator());
                private Option<E> toReturn;

                @Override
                public boolean hasNextSupplier() {
                    final Product2<Option<E>, List<Iterator<BaseTree<E>>>> nextItem;
                    nextItem = stack.nextItem((it, xs) -> {
                        if (it.hasNext()) {
                            BaseTree<E> tree = it.next();
                            if (it.hasNext()) {
                                xs = xs.push(it);
                            }
                            if (tree.isLeaf()) {
                                return Some(Tuple(Some(tree.get()), xs));
                            } else {
                                return Some(Tuple(Nothing(), tree.isEmpty() ? xs : xs.push(tree.branches().iterator())));
                            }
                        }
                        return Nothing();
                    });
                    toReturn = nextItem.$1();
                    stack = nextItem.$2();
                    return !toReturn.isEmpty();
                }

                @Override
                public E nextSupplier() {
                    return toReturn.get();
                }
            };
        }

        @Override
        public String toString() {
            return Iterables.toString('[', ']', iterator());
        }
    }

    class Leaf<E> implements BaseTree<E> {

        private E value;

        private Leaf(E value) {
            this.value = value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public Array<BaseTree<E>> branches() {
            return Array.empty();
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return Iterables.singleton(this::get);
        }

        @Override
        public String toString() {
            return "[" + get() + "]" ;
        }
    }
}
