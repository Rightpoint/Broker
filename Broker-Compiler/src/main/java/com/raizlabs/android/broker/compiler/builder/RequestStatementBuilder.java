package com.raizlabs.android.broker.compiler.builder;

import com.raizlabs.android.broker.compiler.Classes;
import com.raizlabs.android.broker.core.Param;

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
        append("Request request = ");
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

    public RequestStatementBuilder appendBuild() {
        mBuilder.append(".build(requestCallback)");
        return this;
    }

    public RequestStatementBuilder appendProvider(String methodName, String url) {
        mBuilder.append(String.format(".provider(new %1s(getFullBaseUrl(), \"%1s\", Method.%1s))",
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

    public RequestStatementBuilder appendUrlParams(Map<String, Param> urlParams) {
        if(urlParams != null && !urlParams.isEmpty()) {
            Set<String> keys = urlParams.keySet();
            for(String key: keys) {
                Param param = urlParams.get(key);

                appendEmpty();
                mBuilder.append(String.format(".addUrlParam(\"%1s\", ", param.value()));

                if(param.encode()) {
                    mBuilder.append(String.format("%1s.tryEncode(%1s))",
                            Classes.REQUEST_UTILS, key));
                } else {
                    mBuilder.append(String.format("String.valueOf(%1s))", key));
                }
            }
        }

        return this;
    }

    public RequestStatementBuilder appendExecute() {
        append("request.execute()");
        return this;
    }
}
