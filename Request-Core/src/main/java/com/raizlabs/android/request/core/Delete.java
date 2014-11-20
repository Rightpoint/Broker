package com.raizlabs.android.request.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@Target(ElementType.METHOD)
public @interface Delete {

    String value();
}
