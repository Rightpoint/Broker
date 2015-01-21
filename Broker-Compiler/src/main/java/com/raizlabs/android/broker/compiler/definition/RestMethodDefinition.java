package com.raizlabs.android.broker.compiler.definition;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.raizlabs.android.broker.compiler.Classes;
import com.raizlabs.android.broker.compiler.RequestManager;
import com.raizlabs.android.broker.compiler.RequestUtils;
import com.raizlabs.android.broker.compiler.RestParameterMatcher;
import com.raizlabs.android.broker.compiler.WriterUtils;
import com.raizlabs.android.broker.compiler.builder.RequestStatementBuilder;
import com.raizlabs.android.broker.core.Body;
import com.raizlabs.android.broker.core.Endpoint;
import com.raizlabs.android.broker.core.Header;
import com.raizlabs.android.broker.core.Metadata;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RestMethodDefinition implements Definition {

    ExecutableElement element;

    RequestManager requestManager;

    String elementName;

    Method method;

    String url;

    int methodType;

    final Map<String, String> headers = Maps.newHashMap();

    private String[] paramCouples;

    String metaDataParamName;

    /**
     * The name of the variable that is the body
     */
    private String body;

    String responseHandler;

    Map<String, Param> urlParams;

    VariableElement callbackParam;

    String requestCallbackName = "null";

    boolean returnsRequest = false;

    boolean returnsRequestBuilder = false;

    public RestMethodDefinition(RequestManager requestManager, Element inElement) {
        this.requestManager = requestManager;
        method = inElement.getAnnotation(Method.class);
        element = (ExecutableElement) inElement;

        Types types = requestManager.getTypeUtils();
        DeclaredType callbackType = requestManager.getDeclaredType(Classes.REQUEST_CALLBACK,
                types.getWildcardType(null, null));
        DeclaredType requestType = requestManager.getDeclaredType(Classes.REQUEST,
                types.getWildcardType(null, null));
        DeclaredType requestTypeBuilder = requestManager.getDeclaredType(Classes.REQUEST_BUILDER,
                types.getWildcardType(null, null));

        Element returnType = requestManager.getTypeUtils().asElement(element.getReturnType());
        if (returnType != null) {
            returnsRequest = RequestUtils.implementsClassSuper(types, requestType, returnType)
                    || RequestUtils.implementsClass(requestManager.getProcessingEnvironment(), Classes.REQUEST, returnType);

            if(!returnsRequest) {
                returnsRequestBuilder = RequestUtils.implementsClassSuper(types, requestTypeBuilder, returnType)
                        || RequestUtils.implementsClass(requestManager.getProcessingEnvironment(), Classes.REQUEST_BUILDER, returnType);
            }

        }

        elementName = element.getSimpleName().toString();

        url = method.url();

        methodType = method.method();

        Header[] headers = method.headers();
        for (Header header : headers) {
            this.headers.put(header.name(), "\"" + header.value() + "\"");
        }

        if(inElement.getAnnotation(ResponseHandler.class) != null) {
            responseHandler = RequestUtils.getResponseHandler(inElement.getAnnotation(ResponseHandler.class));
        }

        List<? extends VariableElement> params = element.getParameters();
        paramCouples = new String[params.size() * 2];

        List<String> replaceParams = RestParameterMatcher.getMatches(url);

        Map<String, String> endpoints = Maps.newHashMap();

        urlParams = Maps.newHashMap();

        for (int i = 0; i < paramCouples.length; i += 2) {
            VariableElement variableElement = params.get(i / 2);
            Element scrubbed = requestManager.getTypeUtils().asElement(requestManager.getTypeUtils().erasure(variableElement.asType()));
            TypeMirror type = variableElement.asType();
            String name = variableElement.getSimpleName().toString();
            paramCouples[i + 1] = name;
            paramCouples[i] = type.toString();

            // determine if requestcallback is a superclass of the param


            boolean isCallback = RequestUtils.implementsClassSuper(types, callbackType, variableElement)
                    || (scrubbed != null && RequestUtils.implementsClass(requestManager.getProcessingEnvironment(), Classes.REQUEST_CALLBACK, scrubbed));

            // prioritize callbacks
            if(isCallback) {
                callbackParam = variableElement;
                requestCallbackName = callbackParam.getSimpleName().toString();
            } else if (variableElement.getAnnotation(Endpoint.class) != null) {
                endpoints.put(name, name);
            } else if (variableElement.getAnnotation(Header.class) != null) {
                Header header = variableElement.getAnnotation(Header.class);
                this.headers.put(header.value(), name);
            } else if (variableElement.getAnnotation(Body.class) != null) {
                body = name;
            } else if (variableElement.getAnnotation(Param.class) != null) {
                Param param = variableElement.getAnnotation(Param.class);
                urlParams.put(name, param);
            } else if (variableElement.getAnnotation(Metadata.class) != null) {
                metaDataParamName = name;
            }
        }

        String newUrl = url;
        if (replaceParams.size() == endpoints.size()) {
            for (int i = 0; i < replaceParams.size(); i++) {
                String param = replaceParams.get(i);
                newUrl = newUrl.replaceFirst("\\{" + param + "\\}", "\" + " + endpoints.get(param) + " + \"");
            }
        } else {
            requestManager.logError("Parameters for %1s did not match the count of the endpoints defined. " +
                    "Please fix and try again", elementName);
        }

        url = newUrl;
    }

    @Override
    public void write(JavaWriter javaWriter) throws IOException {
        WriterUtils.emitOverriddenMethod(javaWriter, element.getReturnType().toString(),
                element.getSimpleName().toString(),
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        RequestStatementBuilder builder = new RequestStatementBuilder(!returnsRequestBuilder)
                                .appendEmpty().appendRequest().appendEmpty()
                                .appendResponseHandler(responseHandler).appendEmpty();
                        if (!headers.isEmpty()) {
                            builder.appendHeaders(headers).appendEmpty();
                        }

                        if (body != null && !body.isEmpty()) {
                            builder.appendBody(body).appendEmpty();
                        }

                        String method;
                        if (methodType == Method.GET) {
                            method = "GET";
                        } else if (methodType == Method.DELETE) {
                            method = "DELETE";
                        } else if (methodType == Method.POST) {
                            method = "POST";
                        } else if (methodType == Method.PUT) {
                            method = "PUT";
                        } else if (methodType == Method.HEAD) {
                            method = "HEAD";
                        } else if (methodType == Method.OPTIONS) {
                            method = "OPTIONS";
                        } else if (methodType == Method.TRACE) {
                            method = "TRACE";
                        } else if (methodType == Method.PATCH) {
                            method = "PATCH";
                        } else {
                            method = "";
                        }

                        builder.appendProvider(method, url);
                        if (metaDataParamName != null && !metaDataParamName.isEmpty()) {
                            builder.appendEmpty().appendMetaData(metaDataParamName);
                        }

                        builder.appendUrlParams(urlParams);
                        builder.appendEmpty();

                        if(!returnsRequestBuilder) {
                            builder.appendBuild(requestCallbackName);
                        }

                        javaWriter.emitStatement(builder.getStatement());

                        if(!returnsRequest && !returnsRequestBuilder) {
                            javaWriter.emitStatement("request.execute()");
                        } else if(returnsRequest) {
                            javaWriter.emitStatement("return request");
                        } else if(returnsRequestBuilder) {
                            javaWriter.emitStatement("return requestBuilder");
                        }
                    }
                }, paramCouples);
    }
}
