package com.bishabosha.cuppajoe.collections.immutable;

public interface BaseTree<E> {

    @SafeVarargs
    static <E> BaseTree<E> of(E... values) {
        return new Node<>(List.of(values).map(Leaf::new));
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
