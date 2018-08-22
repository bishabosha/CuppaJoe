package com.github.bishabosha.cuppajoe.match.incubator.internal.extract;

import com.github.bishabosha.cuppajoe.match.incubator.patterns.Pattern.PatternVisitor;

public abstract class Extractor implements PatternVisitor {
    protected abstract boolean notInstantiated();
}
