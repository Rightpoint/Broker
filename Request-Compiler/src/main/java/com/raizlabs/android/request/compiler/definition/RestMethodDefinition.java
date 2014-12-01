package com.raizlabs.android.request.compiler.definition;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.raizlabs.android.request.compiler.RequestManager;
import com.raizlabs.android.request.compiler.RestParameterMatcher;
import com.raizlabs.android.request.compiler.WriterUtils;
import com.raizlabs.android.request.compiler.builder.RequestStatementBuilder;
import com.raizlabs.android.request.core.Body;
import com.raizlabs.android.request.core.Endpoint;
import com.raizlabs.android.request.core.Header;
import com.raizlabs.android.request.core.Metadata;
import com.raizlabs.android.request.core.Method;
import com.raizlabs.android.request.core.Param;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

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

    Map<String, Param> urlParams;

    public RestMethodDefinition(RequestManager requestManager, Element inElement) {
        this.requestManager = requestManager;
        method = inElement.getAnnotation(Method.class);
        element = (ExecutableElement) inElement;
        elementName = element.getSimpleName().toString();

        url = method.url();

        methodType = method.method();

        Header[] headers = method.headers();
        for (Header header : headers) {
            this.headers.put(header.name(), "\"" + header.value() + "\"");
        }

        List<? extends VariableElement> params = element.getParameters();
        paramCouples = new String[params.size() * 2];

        List<String> replaceParams = RestParameterMatcher.getMatches(url);

        Map<String, String> endpoints = Maps.newHashMap();

        urlParams = Maps.newHashMap();

        for (int i = 0; i < paramCouples.length; i += 2) {
            VariableElement variableElement = params.get(i / 2);
            TypeMirror type = variableElement.asType();
            String name = variableElement.getSimpleName().toString();
            paramCouples[i + 1] = name;
            paramCouples[i] = type.toString();

            if (variableElement.getAnnotation(Endpoint.class) != null) {
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
                        RequestStatementBuilder builder = new RequestStatementBuilder().appendEmpty()
                                .appendRequest().appendEmpty()
                                .appendResponseHandler().appendEmpty();
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
                        builder.appendEmpty().appendBuild();

                        javaWriter.emitStatement(builder.getStatement());

                        javaWriter.emitStatement("request.execute()");
                    }
                }, paramCouples);
    }
}
