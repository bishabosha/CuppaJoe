/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.cuppajoe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface StateSaver {

    @NotNull
    @Contract(pure = true)
    static <O> StateSaver create(Supplier<O> saver, Consumer<O> restorer) {
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

            @Override
            public String toString() {
                return "<StateSaver>";
            }
        };
    }

    void save();

    void restore();
}
