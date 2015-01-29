package com.raizlabs.android.broker;

import com.raizlabs.android.broker.core.Method;

/**
 * Provides a basic implementation that allows simple Url requests.
 */
public class SimpleUrlProvider implements UrlProvider {

    /**
     * BaseUrl of the request that will prefix the URL of the method when the {@link Request#getUrl()} is called
     */
    private final String mBaseUrl;

    /**
     * The main url of this provider.
     */
    private final String mUrl;

    /**
     * The method of this provider
     */
    private final int mMethod;

    /**
     * Constructs this provider with the specified URL and {@link com.raizlabs.android.broker.core.Method#GET}
     * @param url
     */
    public SimpleUrlProvider(String url) {
        this("", url);
    }

    /**
     * Constructs this provider with the specified base URL, URL, and {@link com.raizlabs.android.broker.core.Method#GET}
     * @param url
     */
    public SimpleUrlProvider(String baseUrl, String url) {
        this(baseUrl, url, Method.GET);
    }

    /**
     * Constructs this provider with the specified URL and {@link com.raizlabs.android.broker.core.Method} int
     * @param url
     */
    public SimpleUrlProvider(String url, int method) {
        this("", url, method);
    }

    /**
     * Constructs this provider with the specified base URL, URL, and {@link com.raizlabs.android.broker.core.Method}
     * @param url
     */
    public SimpleUrlProvider(String baseUrl, String url, int method) {
        mBaseUrl = baseUrl;

        // If combining urls, we should add a leading slash if the url does not contain one.
        if(mBaseUrl !=null && mBaseUrl.length() > 0
                && url != null && !url.startsWith("/")) {
            mUrl = "/" + url;
        } else {
            mUrl = url;
        }
        mMethod = method;
    }

    @Override
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public int getMethod() {
        return mMethod;
    }
}
