/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.trees;

import com.bishabosha.cuppajoe.collections.mutable.base.AbstractSet;
import com.bishabosha.cuppajoe.functions.Equator;
import com.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <ul>
 *     <li>Iterative implementation of an AVL balanced tree.</li>
 * 	   <li>Can be used as a set</li>
 * 	   <li>Can configure the {@code Comparator} used for placing the nodes</li>
 * </ul>
 * @param <E> the element type to store
 */
public class SearchTree<E extends Comparable<E>> extends AbstractSet<E> {
	private BinaryNode<E> root;
	private BinaryNode<E> current;
	private Comparator<E> comparator;

	private BinaryNode<E> createNode(E value) {
		return new BinaryNode<>(comparator, value);
	}
    
    public SearchTree() {
		this(Comparator.naturalOrder());
	}

	public SearchTree(E initial) {
		this(Comparator.naturalOrder(), initial);
	}

	public SearchTree(Comparator<E> comparator, E initial) {
		this(comparator);
		root = createNode(initial);
	}

	public SearchTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		return contains(o, Equator::standardTest);
	}

	protected boolean contains(Object o, Equator<E> equalsTest) {
		try {
			return (o instanceof Comparable<?>) ? null != search((E)o, equalsTest) : false;
		} catch (Exception e) {
			return false;
		}
	}

	public E search(E value) {
		return search(value, Equator::standardTest);
	}
	
	protected E search(E value, Equator<E> equalsTest) {
        current = root;
        while (null != current) {
            if (equalsTest.test(value, current.value)) {
                return current.value;
            }
            current = current.goLeft(value) ? current.left : current.right;
        }
        return null;
    }

	@Override
	protected E replace(E e) {
		return replace(e, Equator::standardTest);
	}

	protected E replace(E value, Equator<E> replaceTest) {
		if (null == root) {
			root = createNode(value);
			size++;
			return null; // no previous value
		}
		NodeRotator rotator = new NodeRotator(value);
		current = root;
		while (true) {
			if (replaceTest.test(value, current.value)) {
				E old = current.value;
				current.value = value; // For map entries with key-only comparisons
				return old;
			}
			rotator.addParent(current);
			boolean insertLeft = current.goLeft(value);
			current = insertLeft ? current.left : current.right;
			if (null == current) {
				if (insertLeft) {
					rotator.topParent().left = createNode(value);
				} else {
					rotator.topParent().right = createNode(value);
				}
				size++;
				break;
			}
		}
		rotator.checkForRotation();
		return null; // no previous value
	}
	
	private class NodeRotator {

		private E value;
		private Deque<BinaryNode<E>> parents = new LinkedList<>();
		
		public NodeRotator(E value) {
			this.value = value;
		}
		
		public void addParent(BinaryNode<E> parent) {
			parents.push(parent);
		}
		
		public BinaryNode<E> topParent() {
			return parents.peek();
		}
		
		public void checkForRotation() {
			while (!parents.isEmpty()) {
				current = parents.pop();
				int balance = current.calculateBalance();

				if (balance < -1 && current.left.goLeft(value)) {
					performRotation(x -> x.rightRotate()); // Left-Left
				} else if (balance < -1 && current.left.goRight(value)) {

					performRotation(x -> { // Left-Right
						x.left = x.left.leftRotate();
						return x.rightRotate();
					});
				} else if (balance > 1 && current.right.goRight(value)) {
					performRotation(x -> x.leftRotate()); // Right-Right
				} else if (balance > 1 && current.right.goLeft(value)) {

					performRotation(x -> { // Right-Left
						x.right = x.right.rightRotate();
						return x.leftRotate();
					});
				}
			}
		}
		
		private void performRotation(Function<BinaryNode<E>, BinaryNode<E>> rotation){
			current = rotation.apply(current);
			BinaryNode<E> parent = topParent();
			if (null == parent) {
				root = current;
			} else if(parent.goLeft(value)) {
				parent.left = current;
			} else {
				parent.right = current;
			}
		}
	}

	@Override
	public E pull(Object entry) {
		return pull(entry, Equator::standardTest);
	}

	protected E pull(Object o, Equator<E> entryEntryTest) {
		E element;
		try {
			element = (E) o;
		} catch (Exception e) {
			return null;
		}
		NodeRemover removal = new NodeRemover(entryEntryTest);
		current = root;
		while(current != null) {
			if (entryEntryTest.test(element, current.value)) {
				E result = current.value;
				removal.removeCurrent();
				size--;
				return result;
			}
			current = removal
				.setParent(current)
				.goLeft(element) ? current.left : current.right;
		}
		return null;
	}
	
	private class NodeRemover {
		private BinaryNode<E> parent;
		private boolean removeLeft = false;
		private Equator<E> entryEntryTest;

		NodeRemover(Equator<E> entryEntryTest) {
			this.entryEntryTest = entryEntryTest;
		}
		
		public NodeRemover setParent(BinaryNode<E> parent) {
			this.parent = parent;
			return this;
		}
		
		public boolean goLeft(E value) {
			return removeLeft = parent.goLeft(value);
		}
		
		public void removeCurrent() {
			int children = current.numChildren();
			if (children < 2) {
				removeNode(children > 0);
			} else {
				removeNodeWithChildren();
			}
		}
		
		private void removeNode(boolean hasChild) {
			boolean leftChild = current.leftNotNull();
			BinaryNode<E> node = hasChild ?
				(leftChild ? current.left : current.right) : null;
			if (entryEntryTest.test(current.value, root.value)) {
				root = node;
			} else if (removeLeft) {
				parent.left = node;
			} else {
				parent.right = node;
			}
		}
		
		private void removeNodeWithChildren() {
			BinaryNode<E> oldCurrent = current;
			parent = current;
			current = current.left;
			removeLeft = true;
			while (current.rightNotNull()) {
				removeLeft = false;
				parent = current;
				current = current.right;
			}
			removeCurrent();
			oldCurrent.value = current.value;
		}
	}
	
	private Consumer<Consumer<E>> preOrderTraversal = consumer -> {
		Deque<BinaryNode<E>> working = new LinkedList<>();
		working.push(root);
		while (!working.isEmpty()) {
			current = working.pop();
			consumer.accept(current.value);
			if (current.rightNotNull()) { // right first as processed last
				working.push(current.right);
			}
			if (current.leftNotNull()) {
				working.push(current.left);
			}
		}
	};
	
	private Consumer<Consumer<E>> postOrderTraversal = consumer -> {
		Deque<BinaryNode<E>> working = new LinkedList<>();
		working.push(root);
		while (!working.isEmpty()) {
			current = working.pop();
			consumer.accept(current.value);
			if (current.leftNotNull()) { // left first as processed last
				working.push(current.left);
			}
			if (current.rightNotNull()) {
				working.push(current.right);
			}
		}
	};
	
	private Consumer<Consumer<E>> inOrderTraversal = consumer -> {
		Deque<BinaryNode<E>> working = new LinkedList<>();
		current = root;
		do {				
			while (current != null) {
				working.push(current);
				current = current.left;
			}
			current = working.pop();
			consumer.accept(current.value);
			current = current.right;
		} while (current != null || (current == null && !working.isEmpty()));
	};
	
	private Consumer<Consumer<E>> levelOrderTraversal = consumer -> {
		Queue<BinaryNode<E>> working = new LinkedList<>();
		working.add(root);
		while (!working.isEmpty()) {
			current = working.remove();
			consumer.accept(current.value);
			if (current.leftNotNull()) {
				working.add(current.left);
			}
			if (current.rightNotNull()) {
				working.add(current.right);
			}
		}
	};
	
	public Iterable<E> preOrder() {
		return valuesIterable(preOrderTraversal, true);
	}
	
	public Iterable<E> postOrder() {
		return valuesIterable(postOrderTraversal, false);
	}
	
	public Iterable<E> inOrder() {
		return valuesIterable(inOrderTraversal, true);
	}
	
	public Iterable<E> levelOrder() {
		return valuesIterable(levelOrderTraversal, true);
	}
	
	private Iterable<E> valuesIterable
		(Consumer<Consumer<E>> traversalDelegate, boolean addRear) {
		// TODO make this lazy
			Deque<E> result = new LinkedList<>();
			if (root != null) {
				traversalDelegate.accept(
					addRear ? 
						val -> result.addLast(val) :
						val -> result.addFirst(val)
				);
			}
			return result;
		}

	@Override
	public Iterator<E> iterator() {
		return inOrder().iterator();
	}

	@Override
	public void clear() {
		root = null;
	}
}
