/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.hashtables;

import java.util.Collection;
import java.util.Iterator;

import com.bishabosha.caffeine.base.AbstractSet;
import com.bishabosha.caffeine.base.Equator;
import com.bishabosha.caffeine.lists.LinkedList;
import com.bishabosha.caffeine.sequences.SequenceOf;
import com.bishabosha.caffeine.sequences.Sequence;

public class HashTable<E> extends AbstractSet<E> {
	private Sequence<Long> primeSequence = SequenceOf.primes();
	private Object[] data;

	private static final int LOAD_FACTOR = 3;
	private static final int MIN_CAPACITY = 3;
	private int numCollisions = 0;

	public HashTable(Collection<E> other) {
		ensureCapacity(other.size());
		addAll(other);
	}

	public HashTable() {
		this(0);
	}

	public HashTable(int expectedCapacity) {
		ensureCapacity(expectedCapacity);
	}

	public E replace(E newEntry) {
		return replace(newEntry, Equator::standardTest);
	}

	protected E replace(E newEntry, Equator<E> replaceTest) {
		E old = addToArrayUnguarded(newEntry, data, replaceTest);
		if (size * LOAD_FACTOR >= data.length) {
			ensureCapacity(size);
		}
		return old;
	}

	private E addToArrayUnguarded(E newEntry, Object[] data, Equator<E> replaceTest) {
		int index = hash(newEntry, data);
		LinkedList<E> list = getList(data, index);
		if (list == null) {
			list = new LinkedList<>();
			data[index] = list;
		}
		for (E element: list) { // prevent repeat values
			if (replaceTest.apply(newEntry, element)) {
				list.replaceCurrent(newEntry);
				return element;
			}
		}
		if (list.size() > 0) {
			numCollisions++;
		}
		list.addLast(newEntry);
		size++;
		return null;
	}

	public boolean contains(Object entry) {
		return null != get(entry, data, Equator::standardTest);
	}

	protected boolean contains(Object entry, Equator<E> containsTest) {
		return null != get(entry, data, containsTest);
	}

	public E get(Object entry) {
		return get(entry, data, Equator::standardTest);
	}

	protected E get(Object entry, Equator<E> getTest) {
		return get(entry, data, getTest);
	}

	private E get(Object entry, Object[] data, Equator<E> getTest) {
		if (entry == null) {
			return null;
		}
		LinkedList<E> list = getList(data, hash(entry, data));
		if (null == list || list.size() == 0) {
			return null;
		}
		for (E storedEntry: list) {
			if (storedEntry.hashCode() == entry.hashCode()
					&& getTest.apply(entry, storedEntry)) {
				return storedEntry;
			}
		}
		return null;
	}

	@Override
	public E pull(Object entry) {
		return pull(entry, Equator::standardTest);
	}

	protected E pull(Object entry, Equator<E> equalsTest) {
		if (data.length > size * LOAD_FACTOR * LOAD_FACTOR) {
			ensureCapacity(size);
		}
		LinkedList<E> list = getList(data, hash(entry));
		if (list == null || list.size() == 0) {
			return null;
		}
		for (E storedEntry: list) {
			if (storedEntry.hashCode() == entry.hashCode()
					&& equalsTest.apply(entry, storedEntry)) {
				if (list.size() > 1) {
					numCollisions--;
				}
				E old = list.currentValue();
				list.removeCurrentValue();
				size--;
				return old;
			}
		}
		return null;
	}

	private HashTable<E> ensureCapacity(int capacity) {
		int newSize;
		if (capacity * LOAD_FACTOR <= MIN_CAPACITY){
			newSize = MIN_CAPACITY;
		} else if (capacity <= 50000){
			newSize = getPrimeLargerThan(LOAD_FACTOR * capacity);
		} else if (capacity >= Integer.MAX_VALUE / LOAD_FACTOR){
			newSize = Integer.MAX_VALUE;
		} else {
			newSize = capacity * LOAD_FACTOR;
		}
		if (null == data) {
			data = new Object[newSize];
		} else {
			copyToNewData(newSize);
		}
		return this;
	}

	private void copyToNewData(int newCapacity) {
		numCollisions = 0;
		size = 0;
		Object[] newData = new Object[newCapacity];
		for (E entry: this) {
			addToArrayUnguarded(entry, newData, Equator::standardTest);
		}
		data = newData;
	}

	private int getPrimeLargerThan(int value) {
		long prime = primeSequence.filter(t -> t > value).term(1).get();
		return prime > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)prime;
	}

	private int hash(Object entry) {
		return hash(entry, data);
	}

	private int hash(Object entry, Object[] data) {
		return Math.abs(entry.hashCode() % data.length);
	}

	@SuppressWarnings("unchecked")
	private LinkedList<E> getList(Object[] array, int index) {
		Object listDelegate = null;
		if (index < array.length) {
			listDelegate = array[index];
		}
		return listDelegate instanceof LinkedList ? (LinkedList<E>) listDelegate : null;
	}

	public int numCollisions() {
		return numCollisions;
	}

	public float currentLoad() {
		return (float) size / (float) data.length;
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {

			private int index = 0;
			private Iterator<E> it = null;
			private E current;

			@Override
			public boolean hasNext() {
				getNextIterator();
				return null == it ? false : it.hasNext();
			}

			@Override
			public E next() {
				return current = it.next();
			}

			private void getNextIterator() {
				while (index < data.length && (null == it || !it.hasNext())) {
					LinkedList<E> list = getList(data, index);
					if (null != list) {
						it = list.iterator();
					}
					index++;
				}
			}

			@Override
			public void remove() {
				HashTable.this.remove(current);
			}
		};
	}

	@Override
	public void clear() {
		numCollisions = 0;
		size = 0;
		data = new Object[0];
		ensureCapacity(MIN_CAPACITY);
	}
}
