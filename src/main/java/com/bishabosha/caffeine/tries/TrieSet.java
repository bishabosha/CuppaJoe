/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.tries;

import java.util.Iterator;
import java.util.Set;
import com.bishabosha.caffeine.base.AbstractCollection;
import com.bishabosha.caffeine.base.AbstractSet;
import com.bishabosha.caffeine.lists.LinkedList;

public class TrieSet extends AbstractSet<String> {
	
	private TrieMap<Object> trie = new TrieMap<>();

	public int numCompletions(String prefix) {
		return trie.numCompletions(prefix);
	}
	
	public LinkedList<String> getCompletions(String prefix) {
		return trie.getCompletions(prefix);
	}
	
	@Override
	public int size() {
		return trie.size();
	}
	
	@Override
	public boolean contains(Object o) {
		return trie.containsKey(o);
	}

	@Override
	public Iterator<String> iterator() {
		return trie.keySet().iterator();
	}

	@Override
	protected String replace(String s) {
		trie.put(s, null);
		return s;
	}

	@Override
	protected String pull(Object entry) {
		if (entry instanceof String) {
			trie.remove(entry);
			return (String)entry;
		}
		return null;
	}

	@Override
	public void clear() {
		trie.clear();
	}

}
