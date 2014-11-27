package com.raizlabs.android.request.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: andrewgrosner
 * Description: Marks a parameter as metadata for the request within a {@link com.raizlabs.android.request.core.RestService}
 * method. Metadata can be a tag, ID, or pretty much anything that identifies this request.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface Metadata {
}
