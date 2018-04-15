/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.tries;

import com.github.bishabosha.cuppajoe.collections.mutable.internal.BasicSet;
import com.github.bishabosha.cuppajoe.collections.mutable.internal.MapEntry;
import com.github.bishabosha.cuppajoe.collections.mutable.lists.LinkedList;
import com.github.bishabosha.cuppajoe.util.Iterators;

import java.util.*;

public class TrieMap<V> extends AbstractMap<String, V> {

    private TrieNode<V> root;
    private int size = 0;

    public TrieMap() {
        this.root = new TrieNode<>(Character.MIN_VALUE);
    }

    public TrieMap(TrieNode<V> root) {
        this.root = root;
    }

    @Override
    public int size() {
        return size;
    }

    public int numCompletions(String prefix) {
        var current = searchNonTerminating(prefix);
        return current == null ? 0 : current.getParentCount();
    }

    public LinkedList<String> getCompletions(String prefix) {
        LinkedList<String> completions = new LinkedList<>();
        var root = searchNonTerminating(prefix);
        if (root == null) {
            return completions;
        }
        var search = new TrieMap<>(root);
        for (var word : search.keySet()) {
            completions.add(prefix.concat(word));
        }
        return completions;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) {
            return false;
        }
        var current = searchNonTerminating((String) key);
        return current != null && current.isTerminating;
    }

    @Override
    public boolean containsValue(Object value) {
        for (var entry : entrySet()) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        var current = searchNonTerminating((String) key);
        return (current != null && current.isTerminating) ? current.value : null;
    }

    protected TrieNode<V> searchNonTerminating(String word) {
        var current = root;
        for (var i = 0; i < word.length(); i++) {
            current = current.getNode(word.charAt(i));
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    @Override
    public V put(String key, V value) {
        var current = root;
        for (var i = 0; i < key.length(); i++) {
            current.addParent();
            current = current.nextNode(key.charAt(i));
        }
        if (!current.isTerminating) {
            current.addParent();
            current.isTerminating = true;
            size++;
        }
        var old = current.value;
        current.value = value;
        return old;
    }

    @Override
    public V remove(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        var word = (String) key;
        V result = null;
        var current = root;
        var lastTerminating = root;
        var rootChar = Character.MIN_VALUE;
        for (var i = 0; i < word.length(); i++) {
            current = current.getNode(word.charAt(i));
            if (current == null) {
                return result;
            }
            if (current.isTerminating) {
                lastTerminating = current;
                rootChar = i < word.length() - 1 ? word.charAt(i + 1) : rootChar;
            }
        }
        result = current.value;
        if (current.hasChildren()) {
            lastTerminating.remove(rootChar);
        } else {
            current.isTerminating = false;
        }
        return result;
    }

    @Override
    public void clear() {
        root = new TrieNode<>(Character.MIN_VALUE);
    }

    public Set<String> keySet() {
        return new BasicSet<>() {

            @Override
            public boolean contains(Object o) {
                return containsKey(o);
            }

            @Override
            public boolean remove(Object o) {
                var before = size();
                TrieMap.this.remove(o);
                return size() < before;
            }

            @Override
            public int size() {
                return TrieMap.this.size();
            }

            @Override
            public void clear() {
                TrieMap.this.clear();
            }

            @Override
            public Iterator<String> iterator() {
                return new Iterator<>() {

                    private Iterator<java.util.Map.Entry<String, V>> it = entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        return it.next().getKey();
                    }
                };
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new BasicSet<>() {

            @Override
            public boolean contains(Object o) {
                return containsValue(o);
            }

            @Override
            public boolean remove(Object o) {
                for (var entry : entrySet()) {
                    if (entry.getValue().equals(o)) {
                        TrieMap.this.remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }

            @Override
            public int size() {
                return TrieMap.this.size();
            }

            @Override
            public void clear() {
                TrieMap.this.clear();
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterators.IdempotentIterator<>() {
                    private Deque<TrieNode<V>> nodeStack = new ArrayDeque<>();
                    private TrieNode<V> currentNode = root;

                    {
                        nodeStack.push(root);
                    }

                    @Override
                    public boolean hasNextSupplier() {
                        while (!nodeStack.isEmpty()) {
                            currentNode = nodeStack.pop();
                            for (var node : currentNode.nextNodes) {
                                nodeStack.push(node);
                            }
                            if (currentNode.isTerminating && currentNode.value != null) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public V nextSupplier() {
                        return currentNode.value;
                    }
                };
            }
        };
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return new BasicSet<>() {

            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Entry) || !(((Entry<?, ?>) o).getKey() instanceof String)) {
                    return false;
                }
                @SuppressWarnings("unchecked")
                var entry = (Entry<String, ?>) o;
                var current = searchNonTerminating(entry.getKey());
                return current != null && current.isTerminating && current.value.equals(entry.getValue());
            }

            @Override
            public boolean remove(Object o) {
                var before = size();
                TrieMap.this.remove(o);
                return size() < before;
            }

            public int size() {
                return TrieMap.this.size();
            }

            public void clear() {
                TrieMap.this.clear();
            }

            @Override
            public Iterator<Entry<String, V>> iterator() {
                return new Iterators.IdempotentIterator<>() {

                    private Deque<TrieNode<V>> nodeStack = new ArrayDeque<>();
                    private Deque<String> stringStack = new ArrayDeque<>();
                    private Entry<String, V> currentEntry;
                    private String currentString = "";
                    private TrieNode<V> currentNode = root;

                    {
                        nodeStack.push(root);
                        stringStack.push(currentString);
                    }

                    @Override
                    public boolean hasNextSupplier() {
                        while (!nodeStack.isEmpty()) {
                            currentNode = nodeStack.pop();
                            currentString = stringStack.pop();
                            currentEntry = new MapEntry<>(currentString, currentNode.value);
                            for (var node : currentNode.nextNodes) {
                                nodeStack.push(node);
                                stringStack.push(currentString + node.unwrap());
                            }
                            if (currentNode.isTerminating) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public Entry<String, V> nextSupplier() {
                        return currentEntry;
                    }
                };
            }
        };
    }

}
