package com.raizlabs.android.request;

import com.raizlabs.android.request.responsehandler.ResponseHandler;

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
     *
     * @return the base url resource id for this interface
     */
    public abstract int getBaseUrlResId();

    public abstract ResponseHandler createResponseHandler();

    public ResponseHandler getResponseHandler() {
        if(mResponseHandler == null) {
            mResponseHandler = createResponseHandler();
        }

        return mResponseHandler;
    }

    public abstract RequestExecutor createRequestExecutor();

    public RequestExecutor getRequestExecutor() {
        if(mRequestExecutor == null) {
            mRequestExecutor = createRequestExecutor();
        }

        return mRequestExecutor;
    }
}
