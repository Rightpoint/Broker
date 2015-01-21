package com.raizlabs.android.broker.compiler.definition;

import com.google.common.collect.Sets;
import com.raizlabs.android.broker.compiler.Classes;
import com.raizlabs.android.broker.compiler.RequestManager;
import com.raizlabs.android.broker.compiler.RequestUtils;
import com.raizlabs.android.broker.compiler.WriterUtils;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RestServiceDefinition extends BaseDefinition {

    static final String DEFINITION_NAME = "$RestService";

    String baseUrlRes;

    int baseUrlResId;

    List<RestMethodDefinition> restMethodDefinitions;

    String responseHandlerClass;

    String requestExecutorClass;

    public RestServiceDefinition(RequestManager requestManager, TypeElement typeElement) {
        super(requestManager, typeElement);
        setDefinitionClassName(DEFINITION_NAME);

        RestService restService = typeElement.getAnnotation(RestService.class);
        baseUrlRes = restService.baseUrl();
        baseUrlResId = restService.baseUrlResId();

        ResponseHandler responseHandler = typeElement.getAnnotation(ResponseHandler.class);
        if (responseHandler != null) {
            responseHandlerClass = RequestUtils.getResponseHandler(responseHandler);
        } else {
            responseHandlerClass = ResponseHandler.class.getCanonicalName();
        }

        RequestExecutor requestExecutor = typeElement.getAnnotation(RequestExecutor.class);
        if (requestExecutor != null) {
            requestExecutorClass = RequestUtils.getRequestExecutor(requestExecutor);
        } else {
            requestExecutorClass = RequestExecutor.class.getCanonicalName();
        }

        restMethodDefinitions = new ArrayList<RestMethodDefinition>();
        List<? extends Element> elements = typeElement.getEnclosedElements();
        for (Element element : elements) {
            if (element.getAnnotation(Method.class) != null) {
                restMethodDefinitions.add(new RestMethodDefinition(requestManager, element));
            }
        }


        List<? extends TypeMirror> interfaces = requestManager.getElements().getTypeElement(getFQCN()).getInterfaces();
        if(!interfaces.isEmpty()) {
            TypeMirror superclass = interfaces.get(0);
            while (superclass != null) {
                TypeElement superElement = ((TypeElement) requestManager.getTypeUtils().asElement(superclass));
                if (superElement != null) {
                    List<? extends Element> superElements = superElement.getEnclosedElements();
                    for (Element element : superElements) {
                        if (element.getAnnotation(Method.class) != null) {
                            restMethodDefinitions.add(new RestMethodDefinition(requestManager, element));
                        }
                    }
                    superclass = superElement.getSuperclass();
                } else {
                    superclass = null;
                }
            }
        }
    }

    @Override
    protected String[] getImports() {
        return new String[]{
                Classes.RESPONSE_HANDLER,
                Classes.REQUEST_EXECUTOR,
                Classes.METHOD,
                Classes.REQUEST
        };
    }

    @Override
    protected void onWriteDefinition(JavaWriter javaWriter) throws IOException {

        WriterUtils.emitOverriddenMethod(javaWriter, "int", "getBaseUrlResId",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        javaWriter.emitStatement("return %1s", baseUrlResId);
                    }
                });

        WriterUtils.emitOverriddenMethod(javaWriter, "String", "getBaseUrl",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        javaWriter.emitStatement("return \"%1s\"", baseUrlRes);
                    }
                });

        WriterUtils.emitOverriddenMethod(javaWriter, "ResponseHandler", "createResponseHandler",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        javaWriter.emitStatement("return new %1s()", responseHandlerClass);
                    }
                });

        WriterUtils.emitOverriddenMethod(javaWriter, "RequestExecutor", "createRequestExecutor",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        javaWriter.emitStatement("return new %1s()", requestExecutorClass);
                    }
                });

        for (RestMethodDefinition restMethodDefinition : restMethodDefinitions) {
            restMethodDefinition.write(javaWriter);
        }
    }

    @Override
    protected String getExtendsClass() {
        return Classes.REST_INTERFACE;
    }

    @Override
    protected String[] getImplementsClasses() {
        return new String[]{elementClassName};
    }
}
