package com.bishabosha.cuppajoe.collections.immutable;

import static com.bishabosha.cuppajoe.API.List;

public interface BaseTree<E> {

    @SafeVarargs
    static <E> BaseTree<E> of(E... values) {
        return new Node<>(Array.of(values).foldRight(List(), (xs, x) -> xs.push(new Leaf<>(x))));
    }

    static <E> BaseTree<E> of(E value) {
        return new Leaf<>(value);
    }

    class Node<E> implements BaseTree<E> {
        List<BaseTree<E>> branches;

        private Node(List<BaseTree<E>> branches) {
            this.branches = branches;
        }
    }

    class Leaf<E> implements BaseTree<E> {

        private E value;

        private Leaf(E value) {
            this.value = value;
        }
    }
}
