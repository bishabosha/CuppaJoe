package com.github.bishabosha.cuppajoe.match;

public class ExtractionFailedException extends RuntimeException {
    public ExtractionFailedException() {
        super("Please check that the correct number of variables can be extracted for the binding expected");
    }

    public ExtractionFailedException(String message) {
        super(message);
    }
}
