/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.lists;

import java.util.function.Predicate;

public abstract class AbstractLinkedList<E> extends AbstractIterableList<E> {

    protected DoublyLinkedNode<E> head;
    protected DoublyLinkedNode<E> tail;
    protected DoublyLinkedNode<E> current;

    public AbstractLinkedList() {
    }

    public AbstractLinkedList(DoublyLinkedNode<E> head, DoublyLinkedNode<E> tail, int size) {
        this.head = head;
        this.tail = tail;
        this.size = size;
    }

    public AbstractLinkedList(AbstractLinkedList<E> other) {
        tail = head = new DoublyLinkedNode<>(other.head);
        addAll(other);
    }

    @Override
    public void reset() {
        current = null;
    }

    @Override
    public E currentValue() {
        return setCurrent() ? current.getValue() : null;
    }

    @Override
    public E replaceCurrent(E element) {
        return replace(element);
    }

    @Override
    protected E replace(E e) {
        var old = currentValue();
        current.setValue(e);
        return old;
    }

    @Override
    public boolean setCurrent() {
        return current != null;
    }

    @Override
    public void insertCurrent(E element) {
        insert(element);
    }

    protected abstract boolean insert(E element);

    @Override
    public boolean setNext() {
        return current != tail && current != null && current.getNext() != null;
    }

    @Override
    public boolean setPrevious() {
        return current != head && current != null && current.getPrevious() != null;
    }

    @Override
    public E removeCurrentIf(Predicate<E> condition) {
        if (condition.test(currentValue())) {
            return removeCurrentValue();
        }
        return null;
    }

    @Override
    public E removeCurrentValue() {
        if (current == head) return removeFront();
        if (current == tail) return removeRear();
        var result = currentValue();
        current = current.removeSelfFromList(true);
        size--;
        return result;
    }

    protected E removeFront() {
        var result = head.getValue();
        current = head = head.removeSelfFromList(true);
        size--;
        return result;
    }

    protected E removeRear() {
        var result = tail.getValue();
        current = tail = tail.removeSelfFromList(false);
        size--;
        return result;
    }

    protected void errorIfEmpty() {
        if (isEmpty()) {
            throw new RuntimeException("empty collection");
        }
    }

    @Override
    public AbstractLinkedList<E> resetCurrentTop() {
        current = head;
        currentIndex = 0;
        return this;
    }

    @Override
    public AbstractLinkedList<E> resetCurrentTail() {
        current = tail;
        currentIndex = size == 0 ? 0 : size - 1;
        return this;
    }

    @Override
    public E advanceForward() {
        current = current == null ? head : current.getNext();
        currentIndex += 1;
        return currentValue();
    }

    @Override
    public E advanceBackward() {
        current = current.getPrevious();
        currentIndex = currentIndex == 0 ? 0 : currentIndex - 1;
        return currentValue();
    }
}
