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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Method {

    public static final int GET = 0;

    public static final int PUT = 1;

    public static final int DELETE = 2;

    public static final int POST = 3;

    String url();

    int method() default GET;

    Header[] headers() default {};
}
