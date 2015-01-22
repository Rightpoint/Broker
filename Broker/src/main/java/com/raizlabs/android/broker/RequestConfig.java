package com.raizlabs.android.broker;

import android.content.Context;

import com.raizlabs.android.broker.responsehandler.ResponseHandler;

/**
 * This is the global area for request configuration.
 */
public class RequestConfig {

    /**
     * The context that all requests use.
     */
    private static Context mContext;

    private static RequestExecutor mRequestExecutor;

    /**
     * Default handler will simply return the response as is.
     */
    private static ResponseHandler mResponseHandler = new ResponseHandler() {

        @Override
        public Object handleResponse(Object o) {
            return o;
        }
    };

    /**
     * Stores the Context passed in. This should be the {@link android.app.Application}
     * context and NOT an Activity.
     *
     * @param context
     * @param defaultExecutor The default request executor to use for any {@link com.raizlabs.android.broker.rest.BaseRestInterface}
     *                        that does not specify a custom executor.
     */
    public static void init(Context context, RequestExecutor defaultExecutor) {
        mContext = context;
        mRequestExecutor = defaultExecutor;
    }

    /**
     * Sets default response handler for all interfaces and requests (if left blank in those classes).
     *
     * @param responseHandler
     */
    public static void setSharedResponseHandler(ResponseHandler responseHandler) {
        mResponseHandler = responseHandler;
    }

    /**
     * @return Shared executor to run all requests by default.
     */
    public static RequestExecutor getSharedExecutor() {
        return mRequestExecutor;
    }

    /**
     * @return Shared response handler that will be used by default for all requests.
     */
    public static ResponseHandler getSharedResponseHandler() {
        return mResponseHandler;
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("You must define a Context for RequestConfig");
        }
        return mContext;
    }
}
