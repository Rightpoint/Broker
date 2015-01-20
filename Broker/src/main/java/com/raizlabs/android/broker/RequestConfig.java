package com.raizlabs.android.broker;

import android.content.Context;

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
     * Stores the Context passed in. This should be the {@link android.app.Application}
     * context and NOT an Activity.
     * @param context
     * @param defaultExecutor The default request executor to use for any {@link com.raizlabs.android.broker.rest.BaseRestInterface}
     *                        that does not specify a custom executor.
     */
    public static void init(Context context, RequestExecutor defaultExecutor) {
        mContext = context;
        mRequestExecutor = defaultExecutor;
    }

    public static RequestExecutor getSharedExecutor() {
        return mRequestExecutor;
    }

    public static Context getContext() {
        if(mContext == null) {
            throw new RuntimeException("You must define a Context for RequestConfig");
        }
        return mContext;
    }
}
