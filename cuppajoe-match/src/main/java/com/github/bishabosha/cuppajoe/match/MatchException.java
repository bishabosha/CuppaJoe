package com.github.bishabosha.cuppajoe.match;

public class MatchException extends RuntimeException {
    public MatchException(Object input) {
        super("No match found for " + input.getClass() + ": " + input);
    }

    public MatchException(Throwable cause) {
        super(cause);
    }

    public MatchException() {
        super("No match");
    }
}
