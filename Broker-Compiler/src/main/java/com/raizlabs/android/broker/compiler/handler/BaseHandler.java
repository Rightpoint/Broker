package com.raizlabs.android.broker.compiler.handler;

import com.raizlabs.android.broker.compiler.RequestManager;
import com.raizlabs.android.broker.compiler.definition.Validator;

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

    private Validator mValidator;

    public BaseHandler(Class<? extends Annotation> annotationClass, Validator validator) {
        this.annotationClass = annotationClass;
        mValidator = validator;
    }

    @Override
    public void handle(RequestManager requestManager, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementSet = roundEnvironment.getElementsAnnotatedWith(annotationClass);

        for(Element element: elementSet) {
            onProcessElement(requestManager, roundEnvironment, (TypeElement) element, mValidator);
        }
    }

    protected abstract void onProcessElement(RequestManager requestManager, RoundEnvironment roundEnvironment,
                                             TypeElement element, Validator validator);
}
