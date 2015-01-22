package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: Marks a parameter as a part in a multipart request as part of a {@link com.raizlabs.android.broker.core.RestService} .
 * Call {@link #isFile()} to true to specify a file path to use for the part.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface Part {

    /**
     * @return the name of the part of multipart request. Only used when part of a {@link com.raizlabs.android.broker.core.Method}
     */
    String name() default "";

    /**
     * @return value of the part when used in a {@link com.raizlabs.android.broker.core.Method}. If used alone,
     * it is the key of the Part.
     */
    String value();

    /**
     * @return true if this part is a file path
     */
    boolean isFile() default false;
}
