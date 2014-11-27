package com.raizlabs.android.request.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Description: Marks a parameter in a {@link com.raizlabs.android.request.core.RestService} method as
 * pertaining to a header for the request.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Header {

    /**
     * The key name of the header to add to the request.
     * @return
     */
    String value();

    /**
     * Optional name for it. Only used in {@link com.raizlabs.android.request.core.Method} definition within
     * a {@link Method#headers()} annotation.
     * @return
     */
    String name() default "";
}
