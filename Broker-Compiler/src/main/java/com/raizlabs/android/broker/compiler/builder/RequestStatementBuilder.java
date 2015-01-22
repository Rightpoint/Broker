package com.raizlabs.android.broker.compiler.builder;

import com.raizlabs.android.broker.compiler.Classes;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.Part;
import com.raizlabs.android.broker.core.Priority;

import java.util.Map;
import java.util.Set;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RequestStatementBuilder  {

    private StringBuilder mBuilder = new StringBuilder();

    public RequestStatementBuilder(boolean request) {
        append(request ? "Request request = " : "Request.Builder requestBuilder = ");
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

    public RequestStatementBuilder appendResponseHandler(String responseHandler) {
        mBuilder.append(String.format(".responseHandler(getResponseHandler(%1s))", responseHandler == null ? null : responseHandler + ".class"));
        return this;
    }

    public String getStatement() {
        return mBuilder.toString();
    }

    public RequestStatementBuilder appendBuild(String requestVariableName) {
        mBuilder.append(String.format(".build(%1s)", requestVariableName));
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

                boolean isHeaderParam = false;
                String keyName = param.value();
                if(param.name() != null && !param.name().isEmpty()) {
                    keyName = param.name();
                    isHeaderParam = true;
                }

                mBuilder.append(String.format(".addUrlParam(\"%1s\", ", keyName));

                String variableName = key;
                if(isHeaderParam) {
                    variableName = "\"" + param.value() + "\"";
                }

                if(param.encode()) {
                    mBuilder.append(String.format("%1s.tryEncode(%1s))",
                            Classes.REQUEST_UTILS, variableName));
                } else {
                    mBuilder.append(String.format("String.valueOf(%1s))", variableName));
                }
            }
        }

        return this;
    }

    public RequestStatementBuilder appendExecute() {
        append("request.execute()");
        return this;
    }

    public RequestStatementBuilder appendParts(Map<String, Part> partMap) {
        if(partMap != null && !partMap.isEmpty()) {
            Set<String> variables = partMap.keySet();
            for(String variableName: variables) {
                Part part = partMap.get(variableName);
                appendEmpty();
                String partKey = part.value();
                boolean isHeaderPart = false;
                if(part.name() != null && !part.name().isEmpty()) {
                    partKey = part.name();
                    isHeaderPart = true;
                }

                String partValue = variableName;
                if(isHeaderPart) {
                    partValue = "\"" + part.value() + "\"";
                }

                mBuilder.append(String.format(".add%sPart(\"%1s\",%1s)", part.isFile() ? "File" : "",
                        partKey, partValue));
            }
        }
        return this;
    }

    public RequestStatementBuilder appendPriority(Priority priority) {
        return append(String.format(".priority(Priority.%1s)", priority.name()));
    }
}
