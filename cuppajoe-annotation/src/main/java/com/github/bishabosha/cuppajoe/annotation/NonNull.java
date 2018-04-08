package com.github.bishabosha.cuppajoe.annotation;

import java.lang.annotation.*;

/**
 * Indicates that the target should never be null.
 * @implSpec When used to target a parameter that is not passed to another method,
 *      where the corresponding argument is also annotated with {@link NonNull},
 *      then that method should assert that the target is not null.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.METHOD})
public @interface NonNull {
}
