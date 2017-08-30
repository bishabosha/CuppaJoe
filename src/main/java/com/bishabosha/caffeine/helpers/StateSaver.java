/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.helpers;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class StateSaver {

    public static final <O> StateSaver create(Supplier<O> saver, Consumer<O> restorer) {
        return new StateSaver() {
            O state;

            @Override
            public void save() {
                state = saver.get();
            }

            @Override
            public void restore() {
                restorer.accept(state);
            }
        };
    }

    public abstract void save();

    public abstract void restore();
}
