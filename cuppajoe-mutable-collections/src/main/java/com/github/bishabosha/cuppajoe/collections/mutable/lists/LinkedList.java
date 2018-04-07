/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.lists;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class LinkedList<E> extends AbstractLinkedList<E> implements Queue<E>, Deque<E> {

    public LinkedList() {
    }

    public LinkedList(LinkedList<E> other) {
        super(other);
    }

    public LinkedList(DoublyLinkedNode<E> head, DoublyLinkedNode<E> tail, int size) {
        super(head, tail, size);
    }

    public boolean insert(E o) {
        return insert(new DoublyLinkedNode<>(o, current, current.getNext()));
    }

    protected boolean insert(DoublyLinkedNode<E> node) {
        if (current == head) {
            return offerFirst(node);
        } else if (current == tail) {
            return offerLast(node);
        }
        current.setNext(node);
        size++;
        return true;
    }

    @Override
    public void addFirst(E o) {
        offerFirst(o);
    }

    @Override
    public void addLast(E e) {
        offerLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        return offerFirst(new DoublyLinkedNode<>(e, null, head));
    }

    protected boolean offerFirst(DoublyLinkedNode<E> node) {
        current = head = node;
        currentIndex = 0;
        if (size++ == 0) {
            tail = head;
        }
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        return offerLast(new DoublyLinkedNode<>(e, tail, null));
    }

    protected boolean offerLast(DoublyLinkedNode<E> node) {
        if (size == 0) {
            return offerFirst(node);
        }
        current = tail = node;
        currentIndex = size;
        size++;
        return true;
    }

    @Override
    public E removeFirst() {
        errorIfEmpty();
        return removeFront();
    }

    @Override
    public E removeLast() {
        errorIfEmpty();
        return removeRear();
    }

    @Override
    public E pollFirst() {
        return isEmpty() ? null : removeFront();
    }

    @Override
    public E pollLast() {
        return isEmpty() ? null : removeRear();
    }

    @Override
    public E getFirst() {
        errorIfEmpty();
        return head.getValue();
    }

    @Override
    public E getLast() {
        errorIfEmpty();
        return tail.getValue();
    }

    @Override
    public E peekFirst() {
        return isEmpty() ? null : head.getValue();
    }

    @Override
    public E peekLast() {
        return isEmpty() ? null : tail.getValue();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (var element : this) {
            if (element.equals(o)) {
                removeCurrentValue();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        var index = -1;
        for (var element : this) {
            if (element.equals(o)) {
                index = currentIndex;
            }
        }
        if (index > 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(E o) {
        return offerLast(o);
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return reversed().iterator();
    }

    @Override
    public void clear() {
        head = tail = null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkBounds(fromIndex);
        checkBounds(toIndex);
        if (fromIndex >= toIndex) {
            throw new IndexOutOfBoundsException("toIndex must be greater than fromIndex");
        }
        goToIndex(fromIndex);
        var newHead = current;
        goToIndex(toIndex - 1);
        return new LinkedList<>(newHead, current, toIndex - fromIndex);
    }
}