package com.github.bishabosha.cuppajoe.util;

public final class ClassUtil {

    private ClassUtil() {
    }

    public static Class<?> wrapperFor(Class<?> primitive) {
        if (!primitive.isPrimitive()) {
            throw new IllegalArgumentException(primitive + " is not primitive");
        }
        if (primitive == boolean.class) {
            return Boolean.class;
        }
        if (primitive == int.class) {
            return Integer.class;
        }
        if (primitive == long.class) {
            return Long.class;
        }
        if (primitive == float.class) {
            return Float.class;
        }
        if (primitive == double.class) {
            return Double.class;
        }
        if (primitive == byte.class) {
            return Byte.class;
        }
        if (primitive == short.class) {
            return Short.class;
        }
        if (primitive == char.class) {
            return Character.class;
        }
        if (primitive == void.class) {
            return Void.class;
        }
        throw new IllegalArgumentException("Unknown primitive " + primitive);
    }
}
