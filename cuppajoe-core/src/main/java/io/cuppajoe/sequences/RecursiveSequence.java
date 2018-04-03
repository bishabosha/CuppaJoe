/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package io.cuppajoe.sequences;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Jamie on 27/02/2017.
 */
public class RecursiveSequence<T> extends AbstractSequence<T> {

    public RecursiveSequence(Function<List<T>, T> algorithm) {
        builder = new RecursiveBuilder(algorithm);
    }

    private class RecursiveBuilder extends AbstractBuilder<T> {
        private Function<List<T>, T> algorithm;

        RecursiveBuilder(Function<List<T>, T> algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        protected T generateTerm() {
            return algorithm.apply(cache);
        }
    }
}
