package com.raizlabs.android.broker.core;

/**
 * Author: andrewgrosner
 * Description: Defines a custom RequestExecutor for a {@link com.raizlabs.android.broker.core.RestService}. The
 * default is a VolleyExecutor.
 */
public @interface RequestExecutor {

    /**
     * @return the class to create as the RequestExecutor. Must implement RequestExecutor and have an
     * available default constructor.
     */
    Class<?> value();
}
