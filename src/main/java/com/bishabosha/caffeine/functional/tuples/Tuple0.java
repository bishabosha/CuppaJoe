/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional.tuples;

import com.bishabosha.caffeine.functional.Pattern;

public class Tuple0 implements Product0 {

    public final static Tuple0 EMPTY = new Tuple0();

    public static Pattern Tuple0() {
        return x -> x instanceof Product0 ? Pattern.PASS : Pattern.FAIL;
    }

    public static Tuple0 getInstance() {
        return EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product0;
    }

    @Override
    public String toString() {
        return "()";
    }
}
