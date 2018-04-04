/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.match.patterns;

import io.cuppajoe.control.Option;
import io.cuppajoe.match.Result;

public interface Pattern<A> {
    Option<Result> test(A obj);
}

