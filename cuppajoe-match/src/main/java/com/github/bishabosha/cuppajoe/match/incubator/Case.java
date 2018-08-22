/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.github.bishabosha.cuppajoe.match.incubator;

import com.github.bishabosha.cuppajoe.control.Option;

/**
 * Represents a Case of a Matcher block,
 * Typically represents a pattern that a variable is matched on, and a mapping function for any variables bound
 *
 * @param <I> the input class being matched
 * @param <O> the output class for if a of is made.
 */
@FunctionalInterface
public interface Case<I, O> {

    /**
     * Attempts to of the Object and Map it
     *
     * @param input the Object being matched
     * @return {@link Option.None} if no of is made. Otherwise {@link Option} of the matched variable
     */
    Option<O> match(I input);

    default O get(I input) throws MatchException {
        return match(input).orElseThrow(() -> new MatchException(input));
    }
}

