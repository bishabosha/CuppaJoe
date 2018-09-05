package com.github.bishabosha.cuppajoe.match.typesafe.internal.extract;

import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.PatternVisitor;

import java.lang.invoke.MethodHandle;
import java.util.ArrayDeque;
import java.util.Deque;

import static com.github.bishabosha.cuppajoe.match.typesafe.internal.extract.Extractors.*;

public abstract class Extractor implements PatternVisitor {

    private MethodHandle matcher = neverMatch();
    private final Deque<MethodHandle> pathStack;

    {
        pathStack = new ArrayDeque<>();
        pushPath(Extractors.identity());
    }

    final MethodHandle popPath() {
        return pathStack.pop();
    }

    final void pushPath(MethodHandle function) {
        pathStack.push(function);
    }

    protected boolean uninitialized() {
        return neverMatches(matcher);
    }

    void appendPredicates(MethodHandle path, MethodHandle predicate) {
        matcher = composePredicates(matcher, composeWithPath(path, predicate));
    }

    public MethodHandle matcher() {
        return matcher;
    }
}

