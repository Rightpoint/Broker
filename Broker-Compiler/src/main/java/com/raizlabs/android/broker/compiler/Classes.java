package com.raizlabs.android.broker.compiler;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class Classes {

    public static final String REQUEST_PACKAGE = "com.raizlabs.android.broker";

    public static final String REST_PACKAGE = REQUEST_PACKAGE + ".rest";

    public static final String REST_INTERFACE = REST_PACKAGE + ".BaseRestInterface";

    public static final String RESPONSE_HANDLER = REQUEST_PACKAGE + ".responsehandler.ResponseHandler";

    public static final String REQUEST_BUILDER = REQUEST_PACKAGE + ".Request.Builder";

    public static final String REQUEST_EXECUTOR = REQUEST_PACKAGE + ".RequestExecutor";

    public static final String SIMPLE_URL_PROVIDER = REQUEST_PACKAGE + ".SimpleUrlProvider";

    public static final String METHOD = REQUEST_PACKAGE + ".core.Method";

    public static final CharSequence REQUEST_MANAGER = REQUEST_PACKAGE + ".RequestManager";

    public static final String REQUEST_MANAGER_ADAPTER = REQUEST_MANAGER + "$Adapter";

    public static final String REST_ADAPTER = REST_PACKAGE + ".RestAdapter";

    public static final String REQUEST_UTILS = REQUEST_PACKAGE + ".RequestUtils";

    public static final String REQUEST = REQUEST_PACKAGE + ".Request";

    public static final String REQUEST_CALLBACK = REQUEST_PACKAGE + ".RequestCallback";
}
