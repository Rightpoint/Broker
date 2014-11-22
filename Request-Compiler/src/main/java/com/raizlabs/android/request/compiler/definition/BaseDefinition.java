package com.raizlabs.android.request.compiler.definition;

import com.google.common.collect.Sets;
import com.raizlabs.android.request.compiler.RequestManager;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public abstract class BaseDefinition implements Definition {

    private final RequestManager requestManager;

    private final Element element;

    String elementClassName;

    private String packageName;

    private String definitionClassName;

    public BaseDefinition(RequestManager requestManager, Element typeElement) {
        this.requestManager = requestManager;
        this.element = typeElement;
        elementClassName = element.getSimpleName().toString();
        packageName = requestManager.getElements().getPackageOf(element).toString();
    }

    @Override
    public void write(JavaWriter javaWriter) throws IOException {
        javaWriter.emitPackage(packageName);
        javaWriter.emitImports(getImports());
        javaWriter.beginType(definitionClassName, "class", Sets.newHashSet(Modifier.PUBLIC, Modifier.FINAL),
                getExtendsClass(), getImplementsClasses());
        onWriteDefinition(javaWriter);

        javaWriter.endType();
        javaWriter.close();
    }

    protected abstract void onWriteDefinition(JavaWriter javaWriter) throws IOException;

    public void setDefinitionClassName(String className) {
        definitionClassName = elementClassName + className;
    }

    public String getSourceFileName() {
        return packageName + "." + definitionClassName;
    }

    protected String[] getImports() {
        return new String[0];
    }

    protected String getExtendsClass() {
        return null;
    }

    protected String[] getImplementsClasses() {
        return new String[0];
    }
}
