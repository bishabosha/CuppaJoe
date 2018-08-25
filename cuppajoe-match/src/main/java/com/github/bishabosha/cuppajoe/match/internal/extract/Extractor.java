package com.github.bishabosha.cuppajoe.match.internal.extract;

import com.github.bishabosha.cuppajoe.match.Path;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.patterns.Pattern.PatternVisitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

import static com.github.bishabosha.cuppajoe.match.internal.extract.Extractors.*;

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

    public Predicate<Object> matcher() {
        return matcher;
    }
}
