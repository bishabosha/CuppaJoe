package com.github.bishabosha.cuppajoe.match.patterns;

public abstract class ResultVisitor {
    public abstract void onValue(Object a);
    public abstract boolean uninitialised();
}
