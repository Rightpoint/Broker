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

    /**
     * Stores the Context passed in. This should be the {@link android.app.Application}
     * context and NOT an Activity.
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        if(mContext == null) {
            throw new RuntimeException("You must define a Context for RequestConfig");
        }
        return mContext;
    }
}
