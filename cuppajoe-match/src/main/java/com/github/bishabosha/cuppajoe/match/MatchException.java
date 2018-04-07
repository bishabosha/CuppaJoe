package com.github.bishabosha.cuppajoe.match;

public class MatchException extends RuntimeException {
    MatchException(Object input) {
        super("No match found for " + input.getClass() + ": " + input);
    }

    MatchException() {
        super("No match found");
    }
}
