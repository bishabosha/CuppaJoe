package com.github.bishabosha.cuppajoe.match.incubator;

public class MatchException extends Exception {
    public MatchException(Object input) {
        super("No match found for " + input.getClass() + ": " + input);
    }
}
