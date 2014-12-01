package com.raizlabs.android.broker.rest;

import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestExecutor;
import com.raizlabs.android.broker.responsehandler.ResponseHandler;
import com.raizlabs.android.broker.volley.VolleyExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Meant for internal use, all interfaces with the RestInterface annotation will generate
 * a class that extends this class.
 */
public abstract class BaseRestInterface {

    private Map<Class<? extends ResponseHandler>, ResponseHandler> mResponseHandlerMap;

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
    public ResponseHandler getResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) {
        ResponseHandler responseHandler;
        if(mResponseHandlerMap == null) {
            mResponseHandlerMap = new HashMap<Class<? extends ResponseHandler>, ResponseHandler>();
        }

        responseHandler = mResponseHandlerMap.get(responseHandlerClass);
        if(responseHandler == null) {
            if(responseHandlerClass == null) {
                responseHandler = createResponseHandler();
            } else {
                try {
                    responseHandler = responseHandlerClass.newInstance();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }

            mResponseHandlerMap.put(responseHandlerClass, responseHandler);
        }

        return responseHandler;
    }

    /**
     * The annotated classes will implement this method automatically (if an annotation is added for it).
     * If no annotation is found, it returns the shared {@link com.raizlabs.android.broker.volley.VolleyExecutor}
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
