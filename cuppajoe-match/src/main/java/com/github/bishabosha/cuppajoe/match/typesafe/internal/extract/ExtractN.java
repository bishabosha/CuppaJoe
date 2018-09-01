package com.github.bishabosha.cuppajoe.match.typesafe.internal.extract;

import com.github.bishabosha.cuppajoe.match.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Branch;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Value;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;

import static com.github.bishabosha.cuppajoe.match.typesafe.internal.extract.Extractors.composePaths;

public final class ExtractN extends Extractor {

    private final MethodHandle[] paths;
    private final int size;
    private int cursor;

    public ExtractN(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size < 0");
        }
        cursor = 0;
        paths = new MethodHandle[size];
        this.size = size;
    }

    @Override
    protected boolean notInstantiated() {
        return super.notInstantiated() || cursor < size;
    }

    @Override
    public final void onEmpty(Empty empty) {
        appendPredicates(popPath(), empty);
    }

    @Override
    public void onValue(Value value) {
        var path = popPath();
        if (cursor >= size) {
            throw new ExtractionFailedException("To many values were found");
        }
        appendPredicates(path, value);
        paths[cursor++] = path;
    }

    @Override
    public final void onBranch(Branch branch) {
        var path = popPath();
        appendPredicates(path, branch);
        branch.pathsAscending().map(composePaths(path)).forEach(this::pushPath);
        branch.visitEachBranch(this);
    }

    public MethodHandle[] getPaths() {
        return Arrays.copyOf(paths, paths.length);
    }
}
