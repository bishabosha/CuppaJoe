package com.github.bishabosha.cuppajoe.match.typesafe.internal.extract;

import com.github.bishabosha.cuppajoe.match.ExtractionFailedException;
import com.github.bishabosha.cuppajoe.match.typesafe.internal.patterns.Bootstraps;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Branch;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Empty;
import com.github.bishabosha.cuppajoe.match.typesafe.patterns.Pattern.Value;
import com.github.bishabosha.cuppajoe.util.ClassUtil;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static com.github.bishabosha.cuppajoe.match.typesafe.internal.extract.Extractors.composePaths;

public final class ExtractN extends Extractor {

    private final Class<?>[] pTypes;
    private final MethodHandle[] paths;
    private final int size;
    private int cursor;

    public ExtractN(List<Class<?>> pTypes) {
        size = pTypes.size();
        this.pTypes = pTypes.toArray(new Class[size]);
        cursor = 0;
        paths = new MethodHandle[size];
    }

    @Override
    protected boolean uninitialized() {
        return super.uninitialized() || cursor < size;
    }

    @Override
    public final void onEmpty(Empty empty) {
        appendPredicates(popPath(), empty.matches());
    }

    @Override
    public void onValue(Value value) {
        var path = popPath();
        if (cursor >= size) {
            throw new ExtractionFailedException("To many values were found");
        }
        if (value == Bootstraps.id() && !pTypes[cursor].isAssignableFrom(path.type().returnType())) {
            var current = path.type().returnType();
            var end = pTypes[cursor];
            if (end.isPrimitive()) {
                var endWrapper = ClassUtil.wrapperFor(end);
                if (!endWrapper.isAssignableFrom(current)) {
                    appendPredicates(path, Modifier.isFinal(endWrapper.getModifiers()) ? Pattern.classEq(endWrapper) : Pattern.classAssignable(endWrapper));
                }
            } else {
                appendPredicates(path, Modifier.isFinal(end.getModifiers()) ? Pattern.classEq(end) : Pattern.classAssignable(end));
            }
        } else {
            appendPredicates(path, value.matches());
        }

        paths[cursor] = path.asType(path.type().changeReturnType(pTypes[cursor++]));
    }

    @Override
    public final void onBranch(Branch branch) {
        var path = popPath();
        appendPredicates(path, branch.matches());
        branch.pathsAscending().map(composePaths(path)).forEach(this::pushPath);
        branch.visitEachBranch(this);
    }

    public MethodHandle[] getPaths() {
        return Arrays.copyOf(paths, paths.length);
    }
}
