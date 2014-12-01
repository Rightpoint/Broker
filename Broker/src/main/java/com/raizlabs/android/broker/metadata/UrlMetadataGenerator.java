package com.raizlabs.android.broker.metadata;

import com.raizlabs.android.broker.Request;

/**
 * Author: andrewgrosner
 * Description: Simple generator that uses the request's URL as the unique ID for the request.
 */
public class UrlMetadataGenerator implements RequestMetadataGenerator {

    @Override
    public Object generate(Request request) {
        return request.getUrl();
    }
}
