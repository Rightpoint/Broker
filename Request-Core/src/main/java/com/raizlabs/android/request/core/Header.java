package com.raizlabs.android.request.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Header {

    /**
     * The key name of the header
     * @return
     */
    String value();

    /**
     * Optional name for it. Only used in {@link com.raizlabs.android.request.core.Method} definition.
     * @return
     */
    String name() default "";
}
