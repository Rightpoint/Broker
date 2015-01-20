package com.raizlabs.android.broker.compiler;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.raizlabs.android.broker.compiler.definition.Definition;
import com.raizlabs.android.broker.compiler.definition.RestServiceDefinition;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Holds all of the RequestService information
 */
public class RequestManager implements Definition{

    private ProcessingEnvironment processingEnvironment;

    private Map<String, RestServiceDefinition> restServiceDefinitionMap = Maps.newHashMap();

    public RequestManager(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public Elements getElements() {
        return processingEnvironment.getElementUtils();
    }

    public void addRestServiceDefinition(RestServiceDefinition restServiceDefinition) {
        restServiceDefinitionMap.put(restServiceDefinition.getSourceFileName(), restServiceDefinition);
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return processingEnvironment;
    }

    public Types getTypeUtils() {
        return processingEnvironment.getTypeUtils();
    }

    public void logError(Throwable t) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, t.getMessage());
    }

    public void logError(String error) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, error);
    }

    @Override
    public void write(JavaWriter javaWriter) throws IOException {
        javaWriter.emitPackage(Classes.REQUEST_PACKAGE);
        javaWriter.emitImports(HashMap.class.getCanonicalName(),
                Map.class.getCanonicalName(),
                Classes.REST_INTERFACE);
        javaWriter.beginType(Classes.REQUEST_MANAGER_ADAPTER, "class",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), Classes.REST_ADAPTER);

        javaWriter.emitField("Map<Class<?>, BaseRestInterface>", "mInterfaceMap",
                Sets.newHashSet(Modifier.PRIVATE, Modifier.FINAL), "new HashMap<Class<?>, BaseRestInterface>()");

        javaWriter.emitEmptyLine();
        javaWriter.beginConstructor(Sets.newHashSet(Modifier.PUBLIC));

        Set<String> restKeySet = restServiceDefinitionMap.keySet();
        for(String key: restKeySet) {
            javaWriter.emitStatement("%1s.put(%1s.class, new %1s())", "mInterfaceMap",
                    restServiceDefinitionMap.get(key).getFQCN(), key);
        }

        javaWriter.endConstructor();

        javaWriter.emitEmptyLine();
        WriterUtils.emitOverriddenMethod(javaWriter, "<RestClass> RestClass", "getRestInterface",
                Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL), new Definition() {
                    @Override
                    public void write(JavaWriter javaWriter) throws IOException {
                        javaWriter.emitStatement("return (%1s) %1s.get(%1s)","RestClass", "mInterfaceMap", "restClass");
                    }
                }, "Class<RestClass>", "restClass");

        javaWriter.endType();
    }

    public Filer getFiler() {
        return processingEnvironment.getFiler();
    }
}
