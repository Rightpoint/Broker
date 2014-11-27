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
 * Description: Meant for internal use, all interfaces with the RestInterface annotation will generate
 * a class that extends this class.
 */
public abstract class BaseRestInterface {

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

    /**
     * The annotated classes will implement this method automatically.
     * @return A new instance of a ResponseHandler.
     */
    public abstract ResponseHandler createResponseHandler();

    /**
     * @return the handler for this interface. Will call {@link #createResponseHandler()} if the response
     * handler has not been created yet.
     */
    public ResponseHandler getResponseHandler() {
        if(mResponseHandler == null) {
            mResponseHandler = createResponseHandler();
        }

        return mResponseHandler;
    }

    /**
     * The annotated classes will implement this method automatically (if an annotation is added for it).
     * If no annotation is found, it returns the shared {@link com.raizlabs.android.request.volley.VolleyExecutor}
     * @return A new request executor.
     */
    public RequestExecutor createRequestExecutor() {
        return VolleyExecutor.getSharedExecutor();
    }

    /**
     * @return The Request executor for this interface. Will call {@link #createRequestExecutor()}
     * if there is none created yet.
     */
    public RequestExecutor getRequestExecutor() {
        if(mRequestExecutor == null) {
            mRequestExecutor = createRequestExecutor();
        }

        return mRequestExecutor;
    }
}
