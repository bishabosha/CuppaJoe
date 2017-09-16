/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.patterns.Pattern;

public class Unit implements Product0 {

    public final static Unit UNIT = new Unit();

    public static Pattern Unit() {
        return x -> x == UNIT ? Pattern.PASS : Pattern.FAIL;
    }

    public static Unit getInstance() {
        return UNIT;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unit && obj == UNIT;
    }

    @Override
    public String toString() {
        return "()";
    }
}
