/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.hashtables;

import io.cuppajoe.Iterators.Lockable;
import io.cuppajoe.collections.mutable.base.AbstractSet;
import io.cuppajoe.collections.mutable.lists.LinkedList;
import io.cuppajoe.sequences.Sequence;
import io.cuppajoe.sequences.SequenceOf;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;

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
		return replace(newEntry, Objects::equals);
	}

	protected E replace(E newEntry, BiPredicate<Object, E> replaceTest) {
        var old = addToArrayUnguarded(newEntry, data, replaceTest);
		if (size * LOAD_FACTOR >= data.length) {
			ensureCapacity(size);
		}
		return old;
	}

	private E addToArrayUnguarded(E newEntry, Object[] data, BiPredicate<Object, E> replaceTest) {
        var index = hash(newEntry, data);
        var list = getList(data, index);
		if (list == null) {
			list = new LinkedList<>();
			data[index] = list;
		}
		for (var element: list) { // prevent repeat values
			if (replaceTest.test(newEntry, element)) {
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
		return null != get(entry, data, Objects::equals);
	}

	protected boolean contains(Object entry, BiPredicate<Object, E> containsTest) {
		return null != get(entry, data, containsTest);
	}

	public E get(Object entry) {
		return get(entry, data, Objects::equals);
	}

	protected E get(Object entry, BiPredicate<Object, E> getTest) {
		return get(entry, data, getTest);
	}

	private E get(Object entry, Object[] data, BiPredicate<Object, E> getTest) {
		if (entry == null) {
			return null;
		}
        var list = getList(data, hash(entry, data));
		if (null == list || list.size() == 0) {
			return null;
		}
		for (var storedEntry: list) {
			if (Objects.equals(Objects.hashCode(storedEntry), Objects.hashCode(entry))
					&& getTest.test(entry, storedEntry)) {
				return storedEntry;
			}
		}
		return null;
	}

	@Override
	public E pull(Object entry) {
		return pull(entry, Objects::equals);
	}

	protected E pull(Object entry, BiPredicate<Object, E> equalsTest) {
		if (data.length > size * LOAD_FACTOR * LOAD_FACTOR) {
			ensureCapacity(size);
		}
        var list = getList(data, hash(entry));
		if (list == null || list.size() == 0) {
			return null;
		}
		for (var storedEntry: list) {
			if (storedEntry.hashCode() == entry.hashCode()
					&& equalsTest.test(entry, storedEntry)) {
				if (list.size() > 1) {
					numCollisions--;
				}
                var old = list.currentValue();
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
        var newData = new Object[newCapacity];
		for (var entry: this) {
			addToArrayUnguarded(entry, newData, Objects::equals);
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
		return new Lockable<>() {

            private int index = 0;
            private Iterator<E> it = null;
            private E current;

            public boolean hasNextSupplier() {
                getNextIterator();
                return null == it ? false : it.hasNext();
            }

            public E nextSupplier() {
                return current = it.next();
            }

            private void getNextIterator() {
                while (index < data.length && (null == it || !it.hasNext())) {
                    var list = getList(data, index);
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
