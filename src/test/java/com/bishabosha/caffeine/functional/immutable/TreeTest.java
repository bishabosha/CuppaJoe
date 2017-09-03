package com.bishabosha.caffeine.functional.immutable;

public class TreeTest {
    public static void main(String[] args) {
        System.out.println(Tree.of(5,3,6,2,4).toJavaList());
        System.out.println(Tree.of(5,3,6,2,4).toCons());
    }
}