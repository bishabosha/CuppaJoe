/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.functional;

import java.util.function.*;

public class Unsafe implements BooleanSupplier {

    private boolean didFail = false;

    private boolean isComputed = false;

    private Throwable throwable = null;

    private Runnable runnable;

    public Unsafe(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * If not computed, will compute the result and store any exceptions or errors given,
     * also memoize the result;
     * @return
     */
    public boolean safeExecute() {
        if (!isComputed) {
            try {
                runnable.run();
            } catch (Throwable err) {
                didFail = true;
                throwable = err;
            }
            isComputed = true;
        }
        return !didFail;
    }

    @Override
    public String toString() {
        return safeExecute() ? "Success" : "Throws("+ throwable.getMessage()+")";
    }

    public boolean getAsBoolean() {
        return safeExecute();
    }

    public Unsafe ifSafe(Unsafe next) {
        if (safeExecute()) {
            return next;
        }
        return this;
    }

    public Option<Throwable> getThrowable() {
        return Option.ofUnknown(throwable);
    }
}
