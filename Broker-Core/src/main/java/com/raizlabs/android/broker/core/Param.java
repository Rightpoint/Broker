package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: Marks a parameter as a URL parameter in a {@link com.raizlabs.android.broker.core.RestService}
 * method. By default each parameter is URL encoded. Call {@link #encode()} to false to use the {@link java.lang.String#valueOf(Object)}
 * instead for the parameter.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface Param {

    /**
     * @return The key for the parameter to add in the request. Only used in {@link com.raizlabs.android.broker.core.Method}
     */
    String name() default "";

    /**
     * Whether to URL encode the value.
     *
     * @return True if we want to encode it for URL, false if we just use it's string value.
     */
    boolean encode() default true;

    /**
     * @return the value of this param when specified in a
     * {@link com.raizlabs.android.broker.core.Method} definition. When is left alone, it is the key
     * of the annotation.
     */
    String value();
}
