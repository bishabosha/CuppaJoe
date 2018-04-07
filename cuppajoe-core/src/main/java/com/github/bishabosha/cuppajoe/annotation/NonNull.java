package com.github.bishabosha.cuppajoe.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the target should never be null.
 * @implSpec When used as a parameter target, methods should assert that it the target is not null
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.METHOD})
public @interface NonNull {
}
