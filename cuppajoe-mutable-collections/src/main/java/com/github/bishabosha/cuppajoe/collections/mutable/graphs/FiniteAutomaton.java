/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.graphs;

import com.github.bishabosha.cuppajoe.collections.mutable.hashtables.HashTable;

import java.util.Arrays;
import java.util.Set;

public abstract class FiniteAutomaton<S, L> extends WeightedGraph<S, L> {
    private Set<L> language;
    private Set<S> finalStates;
    private Set<S> currentStates;
    private S initialState;

    public FiniteAutomaton() {
        language = new HashTable<>();
        finalStates = new HashTable<>();
    }

    public abstract S getNewState();

    public boolean readWord(L[] word, boolean printStages) {
        goToInitialState();
        if (printStages) {
            System.out.println("reading " + Arrays.toString(word));
            System.out.println("Initial State:");
            printState(initialState);
            System.out.println("Final States:");
            printStateSet(finalStates);
        }
        for (L aWord : word) {
            if (!language.contains(aWord))
                throw new IllegalArgumentException("{" + aWord + "} is not accepted by this automaton.");
            var autoFail = !readLetter(aWord);
            if (printStages) {
                System.out.println("Current states after reading {" + aWord + "}:");
                printStateSet(currentStates);
            }
            if (autoFail) return false;
        }
        return isInFinalState();
    }

    private boolean readLetter(L letter) {
        traverseLeapEdges();
        var temp = currentStates;
        currentStates = new HashTable<>();
        for (var state : temp) {
            currentStates.addAll(getRelations(state).getNodesByWeight(letter));
        }
        traverseLeapEdges();
        return currentStates.size() > 0;
    }

    public void traverseLeapEdges() {
        Set<S> holder;
        var temp = currentStates;
        int prev;
        do {
            prev = temp.size();
            holder = new HashTable<>();
            for (var state : temp) {
                holder.addAll(getRelations(state).getLeapRelations());
            }
            temp = holder;
            currentStates.addAll(holder);
        } while (prev != 0 && temp.size() >= prev);
    }

    public FiniteAutomaton<S, L> difference(FiniteAutomaton<S, L> other) {
        return intersection(other.compliment());
    }

    public FiniteAutomaton<S, L> intersection(FiniteAutomaton<S, L> other) {
        return compliment().union(other.compliment()).compliment();
    }

    public FiniteAutomaton<S, L> compliment() {
        var newFinalStates = getStates();
        newFinalStates.removeAll(finalStates);
        finalStates = newFinalStates;
        return this;
    }

    public FiniteAutomaton<S, L> union(FiniteAutomaton<S, L> other) {
        var newInitial = getNewState();
        joinStates(newInitial, initialState);
        joinStates(newInitial, other.initialState);
        setInitialState(newInitial);
        language.addAll(other.language);
        finalStates.addAll(other.finalStates);
        relations.putAll(other.relations);
        return this;
    }

    public FiniteAutomaton<S, L> concatenate(FiniteAutomaton<S, L> other) {
        for (var finalState : finalStates) {
            joinStates(finalState, other.initialState);
        }
        finalStates = new HashTable<>();
        language.addAll(other.language);
        finalStates.addAll(other.finalStates);
        relations.putAll(other.relations);
        return this;
    }

    public FiniteAutomaton<S, L> kleeneStar() {
        var newFinal = getNewState();
        for (var finalState : finalStates) {
            joinStates(finalState, newFinal);
        }
        finalStates = new HashTable<>();
        addFinalState(newFinal);
        joinStates(newFinal, initialState);
        setInitialState(newFinal);
        return this;
    }

    public FiniteAutomaton<S, L> joinStates(S primary, S secondary) {
        return (FiniteAutomaton<S, L>) joinNodes(primary, secondary, true);
    }

    public FiniteAutomaton<S, L> joinStates(S primary, S secondary, L letter) {
        return (FiniteAutomaton<S, L>) joinNodes(primary, secondary, letter, true);
    }

    public FiniteAutomaton<S, L> goToInitialState() {
        currentStates = new HashTable<>();
        currentStates.add(initialState);
        return this;
    }

    public boolean isInFinalState() {
        Set<S> matchingFinalStates = new HashTable<>(finalStates);
        matchingFinalStates.retainAll(currentStates);
        return matchingFinalStates.size() > 0;
    }

    public FiniteAutomaton<S, L> setInitialState(S state) {
        initialState = state;
        addNode(state);
        return this;
    }

    public S getInitialState() {
        return initialState;
    }

    public FiniteAutomaton<S, L> addState(S state) {
        addNode(state);
        return this;
    }

    public Set<S> getStates() {
        return new HashTable<>(relations.keySet());
    }

    public FiniteAutomaton<S, L> addToLanguage(L letter) {
        language.add(letter);
        return this;
    }

    public FiniteAutomaton<S, L> setLanguage(Set<L> language) {
        this.language = language;
        return this;
    }

    public Set<L> getLanguage() {
        return new HashTable<>(language);
    }

    public FiniteAutomaton<S, L> addFinalState(S state) {
        finalStates.add(state);
        addNode(state);
        return this;
    }

    private void printStateSet(Set<S> states) {
        for (var state : states) {
            printState(state);
        }
    }

    private void printState(S state) {
        System.out.println(">>>\t" + state);
    }
}
