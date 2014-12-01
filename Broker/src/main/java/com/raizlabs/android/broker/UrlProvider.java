package com.raizlabs.android.broker;

/**
 *  This class will enable enums and other classes to provide a url for a request.
 *  We use this to standardize the way we run URL requests
 */
public interface UrlProvider {

    /**
     * Return the Base Url for this Provider.
     * @return
     */
    String getBaseUrl();

    /**
     * Return the Fully qualified url for this provider, based on a combination
     * of the base url and endpoint.
     * @return
     */
    String getUrl();

    /**
     * Return the Method that this provider will use.
     * @return
     */
    int getMethod();
}
