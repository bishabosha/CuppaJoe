package com.github.bishabosha.cuppajoe.match.cases;

import com.github.bishabosha.cuppajoe.control.Option;
import com.github.bishabosha.cuppajoe.match.MatchException;

/**
 * Similar to a Case, but only has to optionally supply a value. Guards are different to Cases,
 * as there is no input variable, Guards rely on closures to evaluate application state and supply a result.
 *
 * @param <O> the output Type
 */
@FunctionalInterface
public interface Guard<O> {
    /**
     * User defines guards that supply an optional of a value.
     */
    Option<O> match();

    default O get() {
        return match().orElseThrow(MatchException::new);
    }
}
