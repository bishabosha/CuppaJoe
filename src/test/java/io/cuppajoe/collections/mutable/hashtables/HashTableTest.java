/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.collections.mutable.hashtables;

import io.cuppajoe.Iterators;
import org.junit.Assert;
import org.junit.Test;

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
    public void testRetainAll() {
        var intersection = getGreetings();
        intersection.retainAll(getTwoLetterWords());
        Assert.assertTrue(Iterators.equalElements(intersection, "Hi", "Yo"));
    }

    @Test
    public void testRemoveAll() {
        var difference = getGreetings();
        difference.removeAll(getTwoLetterWords());
        Assert.assertTrue(Iterators.equalElements(difference, "Hello", "Bonjour", "Ciao", "Waddup"));
    }

    @Test
    public void testContainsAll() {
        Assert.assertTrue(getGreetings().containsAll(Iterators.setOf("Hi", "Hello")));
    }
}
