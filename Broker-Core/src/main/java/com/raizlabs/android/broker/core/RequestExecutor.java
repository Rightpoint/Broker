package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: Defines a custom RequestExecutor for a {@link com.raizlabs.android.broker.core.RestService}. The
 * default is a VolleyExecutor.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface RequestExecutor {

    /**
     * @return the class to create as the RequestExecutor. Must implement RequestExecutor and have an
     * available default constructor.
     */
    Class<?> value();
}
