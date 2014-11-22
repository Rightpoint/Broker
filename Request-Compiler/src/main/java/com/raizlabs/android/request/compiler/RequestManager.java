package com.raizlabs.android.request.compiler;

import com.google.common.collect.Maps;
import com.raizlabs.android.request.compiler.definition.RestServiceDefinition;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Holds all of the RequestService information
 */
public class RequestManager {

    private ProcessingEnvironment processingEnvironment;

    private Map<String, RestServiceDefinition> restServiceDefinitionMap = Maps.newHashMap();

    public RequestManager(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public Elements getElements() {
        return processingEnvironment.getElementUtils();
    }

    public void addRestServiceDefinition(RestServiceDefinition restServiceDefinition) {

    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnvironment;
    }

    public void logError(Throwable t) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, t.getMessage());
    }

    public void logError(String error) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, error);
    }
}
