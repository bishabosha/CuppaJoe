/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.projecteuler;

public class IntLoop {
    private int start;
    private int end;
    private int interval;

    private int counter;

    public IntLoop(int start, int end, int interval) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.counter = 1;
    }

    public int getValue(int n) {
        counter = n + 1;
        return IntLoop.getValue(n, start, end, interval);
    }

    public int getNextValue() {
        return IntLoop.getValue(counter++, start, end, interval);
    }

    public static int getValue(int n, int start, int end, int interval) {
        var diff = 1 + end - start;
        return (n / interval) % diff + start;
    }
}