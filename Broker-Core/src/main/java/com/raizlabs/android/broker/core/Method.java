package com.raizlabs.android.broker.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Description: Defines a method within a {@link com.raizlabs.android.broker.core.RestService} class.
 * The method can only either return a Request, or return void with a RequestCallback as the parameter.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Method {

    /**
     * HTTP GET
     */
    public static final int GET = 0;

    /**
     * HTTP POST
     */
    public static final int POST = 1;

    /**
     *
     * HTTP PUT
     */
    public static final int PUT = 2;

    /**
     * HTTP DELETE
     */
    public static final int DELETE = 3;

    /**
     * HTTP HEAD
     */
    public static final int HEAD = 4;

    /**
     * HTTP OPTIONS
     */
    public static final int OPTIONS = 5;

    /**
     * HTTP TRACE
     */
    public static final int TRACE = 6;

    /**
     * HTTP PATCH
     */
    public static final int PATCH = 7;

    /**
     * The url for this request method. Must not contain the base url, it is the second piece that will
     * be combined with the base url. You can specify varying endpoints by inserting it like: "/example/{endPointName}/something"
     * and then the endpoint variable name must match what you put into the braces. Do not include URL parameters here
     * if any specified in the method.
     * @return
     */
    String url();

    /**
     * @return The HTTP method to use.
     */
    int method() default GET;

    /**
     * @return Defines a set of static headers to add to the method. All {@link com.raizlabs.android.broker.core.Header}
     * must have a name defined.
     */
    Header[] headers() default {};

    /**
     * @return The priority for this method's request.
     */
    Priority priority() default Priority.NORMAL;
}
