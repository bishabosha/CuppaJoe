package com.bishabosha.cuppajoe.collections.immutable;

public interface BaseTree<E> {

    class Node<E> implements BaseTree<E> {
        List<BaseTree<E>> branches;
    }

    class Leaf<E> implements BaseTree<E> {
        private E value;
    }
}
