package com.raizlabs.android.request.rest;

import com.raizlabs.android.request.RequestConfig;
import com.raizlabs.android.request.RequestExecutor;
import com.raizlabs.android.request.metadata.RequestMetadataGenerator;
import com.raizlabs.android.request.metadata.UrlMetadataGenerator;
import com.raizlabs.android.request.responsehandler.ResponseHandler;
import com.raizlabs.android.request.volley.VolleyExecutor;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public abstract class RestInterface {

    private ResponseHandler mResponseHandler;

    private RequestExecutor mRequestExecutor;

    /**
     * @return the base url for this interface
     */
    public abstract String getBaseUrl();

    /**
     * @return the base url resource id for this interface
     */
    public abstract int getBaseUrlResId();

    /***
     * @return Resolves if we are using a resource ID or actual string for the url.
     */
    public String getFullBaseUrl() {
        String url = getBaseUrl();
        if(getBaseUrlResId() != 0) {
            url = RequestConfig.getContext().getString(getBaseUrlResId());
        }

        return url;
    }

    public abstract ResponseHandler createResponseHandler();

    public ResponseHandler getResponseHandler() {
        if(mResponseHandler == null) {
            mResponseHandler = createResponseHandler();
        }

        return mResponseHandler;
    }

    public RequestExecutor createRequestExecutor() {
        return new VolleyExecutor();
    }

    public RequestExecutor getRequestExecutor() {
        if(mRequestExecutor == null) {
            mRequestExecutor = createRequestExecutor();
        }

        return mRequestExecutor;
    }
}
