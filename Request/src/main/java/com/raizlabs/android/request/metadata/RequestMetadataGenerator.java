package com.raizlabs.android.request.metadata;

import com.raizlabs.android.request.Request;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Class used in generating metadata for a whole category of Requests. Typically this should
 * be used in {@link com.raizlabs.android.request.core.RestService}
 */
public interface RequestMetadataGenerator {

    public Object generate(Request request);
}
