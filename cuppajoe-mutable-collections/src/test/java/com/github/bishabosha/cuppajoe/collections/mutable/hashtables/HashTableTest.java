/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.collections.mutable.hashtables;

import com.github.bishabosha.cuppajoe.util.Iterators;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashTableTest {

    HashTable<String> getGreetings() {
        HashTable<String> greetings = new HashTable<>();
        greetings.add("Hello");
        greetings.add("Hi");
        greetings.add("Yo");
        greetings.add("Bonjour");
        greetings.add("Ciao");
        greetings.add("Waddup");
        return greetings;
    }

    HashTable<String> getTwoLetterWords() {
        HashTable<String> twoLetterWords = new HashTable<>();
        twoLetterWords.add("OK");
        twoLetterWords.add("Yo");
        twoLetterWords.add("Hi");
        twoLetterWords.add("My");
        return twoLetterWords;
    }

    @Test
    public void test_RetainAll() {
        var intersection = getGreetings();
        intersection.retainAll(getTwoLetterWords());
        assertTrue(Iterators.equalElements(intersection, "Hi", "Yo"));
    }

    @Test
    public void test_RemoveAll() {
        var difference = getGreetings();
        difference.removeAll(getTwoLetterWords());
        assertTrue(Iterators.equalElements(difference, "Hello", "Bonjour", "Ciao", "Waddup"));
    }

    @Test
    public void test_ContainsAll() {
        assertTrue(getGreetings().containsAll(List.of("Hi", "Hello")));
    }
}
