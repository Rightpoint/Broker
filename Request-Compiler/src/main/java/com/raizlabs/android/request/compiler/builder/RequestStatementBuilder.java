package com.raizlabs.android.request.compiler.builder;

import com.raizlabs.android.request.compiler.Classes;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RequestStatementBuilder  {

    private StringBuilder mBuilder = new StringBuilder();

    public RequestStatementBuilder() {
    }

    public RequestStatementBuilder(String str) {
        mBuilder.append(str);
    }

    public RequestStatementBuilder appendEmpty() {
        append("\n");
        return this;
    }

    public RequestStatementBuilder append(Object object) {
        mBuilder.append(object);
        return this;
    }

    public RequestStatementBuilder appendRequest() {
        mBuilder.append(String.format("new %1s(%1s)", Classes.REQUEST_BUILDER, "getRequestExecutor()"));
        return this;
    }

    public RequestStatementBuilder appendResponseHandler() {
        mBuilder.append(String.format(".responseHandler(getResponseHandler())"));
        return this;
    }

    public String getStatement() {
        return mBuilder.toString();
    }

    public RequestStatementBuilder appendExecute() {
        mBuilder.append(".execute(requestCallback)");
        return this;
    }

    public RequestStatementBuilder appendProvider(String methodName, String url) {
        mBuilder.append(String.format(".provider(new %1s(getBaseUrl(), \"%1s\", Method.%1s))",
                Classes.SIMPLE_URL_PROVIDER, url, methodName));
        return this;
    }

    public RequestStatementBuilder appendHeaders(Map<String, String> headers) {
        Set<String> headerSet = headers.keySet();
        for(String header: headerSet) {
            mBuilder.append(String.format(".addRequestHeader(\"%1s\", %1s)", header, headers.get(header)));
        }

        return this;
    }

    public RequestStatementBuilder appendBody(String bodyName) {
        mBuilder.append(String.format(".body(%1s)", bodyName));
        return this;
    }

    public RequestStatementBuilder appendMetaData(String metaDataParamName) {
        mBuilder.append(String.format(".metaData(%1s)", metaDataParamName));
        return this;
    }
}
