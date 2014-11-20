package com.raizlabs.android.request;

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
    private final Request.Method mMethod;

    /**
     * Constructs this provider with the specified URL and {@link Request.Method#GET}
     * @param url
     */
    public SimpleUrlProvider(String url) {
        this("", url);
    }

    /**
     * Constructs this provider with the specified base URL, URL, and {@link Request.Method#GET}
     * @param url
     */
    public SimpleUrlProvider(String baseUrl, String url) {
        this(baseUrl, url, Request.Method.GET);
    }

    /**
     * Constructs this provider with the specified URL and {@link Request.Method}
     * @param url
     */
    public SimpleUrlProvider(String url, Request.Method method) {
        this("", url, method);
    }

    /**
     * Constructs this provider with the specified base URL, URL, and {@link Request.Method}
     * @param url
     */
    public SimpleUrlProvider(String baseUrl, String url, Request.Method method) {
        mBaseUrl = baseUrl;
        mUrl = url;
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
    public Request.Method getMethod() {
        return mMethod;
    }
}
