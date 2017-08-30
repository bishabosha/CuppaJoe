/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.hashtables;

import com.bishabosha.caffeine.base.Iterables;
import com.bishabosha.caffeine.hashtables.HashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    void testRetainAll() {
        HashTable<String> intersection = getGreetings();
        intersection.retainAll(getTwoLetterWords());
        Assertions.assertTrue(Iterables.equalElements(intersection, "Hi", "Yo"));
    }

    @Test
    void testRemoveAll() {
        HashTable<String> difference = getGreetings();
        difference.removeAll(getTwoLetterWords());
        Assertions.assertTrue(Iterables.equalElements(difference, "Hello", "Bonjour", "Ciao", "Waddup"));
    }

    @Test
    void testContainsAll() {
        Assertions.assertTrue(getGreetings().containsAll(Iterables.setOf("Hi", "Hello")));
    }
}
