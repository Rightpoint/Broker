package com.raizlabs.android.request.compiler.handler;

import com.raizlabs.android.request.compiler.RequestManager;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public abstract class BaseHandler implements Handler {

    private Class<? extends Annotation> annotationClass;

    public BaseHandler(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public void handle(RequestManager requestManager, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(annotationClass);

        for(Element element: elementSet) {
            onProcessElement(requestManager, roundEnvironment, (TypeElement) element);
        }
    }

    protected abstract void onProcessElement(RequestManager requestManager, RoundEnvironment roundEnvironment, TypeElement element);
}
