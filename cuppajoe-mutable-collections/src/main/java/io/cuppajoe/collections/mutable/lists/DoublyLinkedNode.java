/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.lists;

public class DoublyLinkedNode<E> {

    private E value;
    private DoublyLinkedNode<E> previous;
    private DoublyLinkedNode<E> next;

    public DoublyLinkedNode() {
        previous = null;
        next = null;
    }

    DoublyLinkedNode(DoublyLinkedNode<E> old) {
        this.value = old.value;
    }

    DoublyLinkedNode(E value, DoublyLinkedNode<E> previous, DoublyLinkedNode<E> next) {
        this.value = value;
        this.previous = previous;
        this.next = next;
        if (previous != null) {
            previous.next = this;
        }
        if (next != null) {
            next.previous = this;
        }
    }

    public DoublyLinkedNode<E> removeSelfFromList(boolean returnNextNode) {
        var result = returnNextNode ? this.next : this.previous;
        if (this.next != null) {
            this.next.previous = this.previous;
        }
        if (this.previous != null) {
            this.previous.next = this.next;
        }
        this.next = null;
        this.previous = null;
        return result;
    }

    public void setNext(DoublyLinkedNode<E> next) {
        this.next = next;
    }

    public DoublyLinkedNode<E> getNext() {
        return next;
    }

    public void setPrevious(DoublyLinkedNode<E> previous) {
        this.previous = previous;
    }

    public DoublyLinkedNode<E> getPrevious() {
        return previous;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }
}
