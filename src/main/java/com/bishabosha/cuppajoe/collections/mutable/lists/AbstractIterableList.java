/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.lists;

import com.bishabosha.cuppajoe.Iterables;
import com.bishabosha.cuppajoe.collections.mutable.base.AbstractCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractIterableList<E> extends AbstractCollection<E> implements IterableList<E> {
	protected int currentIndex = 0;

	@Override
	public int currentIndex() {
		return currentIndex;
	}

	@Override
	public E get(int i) {
		checkBounds(i);
		goToIndex(i);
		return currentValue();
	}

	@Override
	public E set(int index, E element) {
		goToIndex(index);
		return replaceCurrent(element);
	}

	@Override
	public void add(int index, E element) {
		goToIndex(index);
		insertCurrent(element);
	}

	@Override
	protected E pull(Object o) {
		for(E val: this) {
			if (val.equals(o)) {
				E old = currentValue();
				removeCurrentValue();
				return old;
			}
		}
		return null;
	}

	@Override
	public boolean contains(Object o) {
		for (E element: this) {
			if (element.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		goToIndex(index);
		int before = size();
		for (E element: c) {
			insertCurrent(element);
		}
		return before < size();
	}

	@Override
	public E remove(int i) {
		checkBounds(i);
		goToIndex(i);
		return removeCurrentValue();
	}

	@Override
	public int indexOf(Object o) {
		for (E element: this) {
			if (element.equals(o)) {
				return currentIndex;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int index = -1;
		for (E element: this) {
			if (element.equals(o)) {
				index = currentIndex;
			}
		}
		return index;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new Literator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new Literator() {
			{
				goToIndex(index);
			}
		};
	}

	private class Literator implements ListIterator<E>{
		{
			reset();
		}
		@Override
		public boolean hasNext() {
			return setNext();
		}

		@Override
		public E next() {
			return advanceForward();
		}

		@Override
		public boolean hasPrevious() {
			return setPrevious();
		}

		@Override
		public E previous() {
			return advanceBackward();
		}

		@Override
		public int nextIndex() {
			return hasNext() ? currentIndex + 1 : size();
		}

		@Override
		public int previousIndex() {
			return hasPrevious() ? currentIndex - 1 : -1;
		}

		@Override
		public void remove() {
			removeCurrentValue();
		}

		@Override
		public void set(E e) {
			replaceCurrent(e);
		}

		@Override
		public void add(E e) {
			insertCurrent(e);
		}
	}

	protected void checkBounds(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException(index + " is out of range");
		}
	}

	@Override
	public void goToIndex(int index) {
		checkBounds(index);
		if (index != currentIndex) {
			if (index == 0) {
				resetCurrentTop();
			} else if (index == size - 1) {
				resetCurrentTail();
			} else if (index > currentIndex) {
				while (currentIndex != index) {
					advanceForward();
				}
			} else {
				while (currentIndex != index) {
					advanceBackward();
				}
			}
		} 
	}

	@Override
	public String toString() {
		return toString(this);
	}
	
	public String toReverseString() {
		return toString(reversed());
	}
	
	public String toString(Iterable<E> list) {
		StringBuilder result = new StringBuilder("[");
		int counter = 0;
		for (E element: list) {
			result.append(++counter < size() ? element + ", " : element);
		}
		return result.append("]").toString();
	}

	@Override
	public Iterator<E> iterator() {
		resetCurrentTop();
		return new Iterables.Lockable<>() {
            boolean returnHead = true;

            @Override
            public boolean hasNextSupplier() {
                return returnHead ? setCurrent() : setNext();
            }

            @Override
            public E nextSupplier() {
                if (returnHead) {
                    returnHead = false;
                    return currentValue();
                }
                return advanceForward();
            }

            @Override
            public void remove() {
                AbstractIterableList.this.removeCurrentValue();
            }
        };
	}

	@Override
	public Iterable<E> reversed() {
		resetCurrentTail();
		return () -> new Iterables.Lockable<>() {
            boolean returnTail = true;

            @Override
            public boolean hasNextSupplier() {
                return returnTail ? setCurrent() : setPrevious();
            }

            @Override
            public E nextSupplier() {
                if (returnTail) {
                    returnTail = false;
                    return currentValue();
                }
                return advanceBackward();
            }
        };
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof List && Iterables.equals(this, obj);
	}
}
