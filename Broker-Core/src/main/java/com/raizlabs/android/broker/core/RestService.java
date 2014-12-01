package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Description: Marks the class to generate a $RestService definition.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RestService {

    /**
     * Specify a url resource Id to return for this service
     * @return
     */
    int baseUrlResId() default 0;

    /**
     * Specify a url String to return for this service
     * @return
     */
    String baseUrl() default "";

}
