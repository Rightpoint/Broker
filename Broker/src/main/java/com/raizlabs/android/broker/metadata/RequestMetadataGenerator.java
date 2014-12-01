package com.raizlabs.android.broker.metadata;

import com.raizlabs.android.broker.Request;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Class used in generating metadata for a whole category of Requests. Typically this should
 * be used in {@link com.raizlabs.android.broker.core.RestService}
 */
public interface RequestMetadataGenerator {

    public Object generate(Request request);
}
