package com.raizlabs.android.broker.webservicemanager;

import com.raizlabs.android.broker.core.Method;
import com.raizlabs.net.HttpMethod;

/**
 * Description: Provides utility methods for interacting with WebServiceManager
 */
public class WebServiceManagerUtils {

    public static HttpMethod convertMethodIntToMethod(int method) {
        switch (method) {
            case Method.GET:
                return HttpMethod.Get;
            case Method.PUT:
                return HttpMethod.Put;
            case Method.DELETE:
                return HttpMethod.Delete;
            case Method.HEAD:
                return HttpMethod.Head;
            case Method.POST:
                return HttpMethod.Post;
            default:
                return null;
        }
    }
}
