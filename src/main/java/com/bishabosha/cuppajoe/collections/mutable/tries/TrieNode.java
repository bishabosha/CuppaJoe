/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe.collections.mutable.tries;

import com.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;

public class TrieNode<T> {
	
	T value = null;
	private int parentWordCount = 0;
	boolean isTerminating = false;
	private char wrapped;
	LinkedList<TrieNode<T>> nextNodes = new LinkedList<>();
	
	public TrieNode(char toWrap) {
		wrapped = toWrap;
	}
	
	public boolean hasChildren() {
		return !nextNodes.isEmpty();
	}
	
	public boolean isForChar(char toCheck) {
		return toCheck == wrapped;
	}
	
	public char unwrap() {
		return wrapped;
	}
	
	public int getParentCount() {
		return parentWordCount;
	}
	
	public void addParent() {
		parentWordCount++;
	}
	
	public void remove(char toRemove) {
		for (TrieNode<T> node: nextNodes) {
			if (node.isForChar(toRemove)) {
				parentWordCount--;
				nextNodes.remove(node);
			}
		}
	}
	
	public TrieNode<T> nextNode(char toAdd) {
		TrieNode<T> result = getNode(toAdd);
		if (result != null) {
			return result;
		}
		result = new TrieNode<>(toAdd);
		nextNodes.add(result);
		return result;
	}
	
	public TrieNode<T> getNode(char toGet) {
		for (TrieNode<T> node: nextNodes) {
			if (node.isForChar(toGet)) {
				return node;
			}
		}
		return null;
	}
}
