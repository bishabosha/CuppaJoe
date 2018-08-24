package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.match.incubator.Path;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.PatternVisitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.match.incubator.internal.extract.Extractors.*;

public abstract class Extractor implements PatternVisitor {

    private Predicate<Object> matcher = neverMatch();
    private Deque<Path> pathStack = new ArrayDeque<>();

    {
        pushPath(Path.identity());
    }

    final Path popPath() {
        return pathStack.pop();
    }

    final void pushPath(Path function) {
        pathStack.push(function);
    }

    protected boolean notInstantiated() {
        return neverMatches(matcher);
    }

    <T> void appendPredicates(Path path, Pattern<T> pattern) {
        matcher = composePredicates(matcher, composeWithPath(path, pattern.matches()));
    }

    protected Predicate<Object> matcher() {
        return matcher;
    }
}
