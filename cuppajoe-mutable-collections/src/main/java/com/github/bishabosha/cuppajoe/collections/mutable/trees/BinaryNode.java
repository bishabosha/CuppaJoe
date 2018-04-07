/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.trees;

import java.util.Comparator;

public class BinaryNode<T extends Comparable<T>> {

    Comparator<T> comparator;
    T value;
    BinaryNode<T> left;
    BinaryNode<T> right;

    public BinaryNode<T> getLeft() {
        return left;
    }

    public BinaryNode<T> getRight() {
        return right;
    }

    public BinaryNode<T> addLeft(BinaryNode<T> left) {
        return this.left = left;
    }

    public BinaryNode<T> addRight(BinaryNode<T> right) {
        return this.right = right;
    }

    public T getValue() {
        return value;
    }

    private int height = 0;

    public BinaryNode(T value) {
        this(Comparator.naturalOrder(), value);
    }

    public BinaryNode(Comparator<T> comparator, T value) {
        this.value = value;
        this.comparator = comparator;
    }

    public void calculateHeight() {
        height = 1 + Math.max(heightOf(left), heightOf(right));
    }

    public int calculateBalance() {
        calculateHeight();
        return heightOf(right) - heightOf(left);
    }

    public int heightOf(BinaryNode<T> node) {
        return node != null ? node.getHeight() : -1;
    }

    public int getHeight() {
        return height;
    }

    public BinaryNode<T> leftRotate() {
        var newRoot = right;
        var temp = right.left;
        right.left = this;
        right = temp;

        calculateHeight();
        if (null != right) {
            right.calculateHeight();
        }

        return newRoot;
    }

    public BinaryNode<T> rightRotate() {
        var newRoot = left;
        var temp = left.right;
        left.right = this;
        left = temp;

        calculateHeight();
        if (null != left) {
            left.calculateHeight();
        }

        return newRoot;
    }

    public boolean goLeft(T newValue) {
        return comparator.compare(value, newValue) == 1;
    }

    public boolean goRight(T newValue) {
        return !goLeft(newValue);
    }

    public boolean isLeaf() {
        return numChildren() == 0;
    }

    public int numChildren() {
        var result = 0;
        if (leftNotNull()) result++;
        if (rightNotNull()) result++;
        return result;
    }

    public boolean leftNotNull() {
        return null != left;
    }

    public boolean rightNotNull() {
        return null != right;
    }

    @Override
    public String toString() {
        return "[" + (left == null ? "" : left + ", ") + value + (right == null ? "" : ", " + right) + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof BinaryNode) && (((BinaryNode<?>) obj).value.equals(value)));
    }
}