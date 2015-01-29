package com.raizlabs.android.broker.compiler.handler;

import com.raizlabs.android.broker.compiler.RequestManager;
import com.raizlabs.android.broker.compiler.definition.RestServiceDefinition;
import com.raizlabs.android.broker.compiler.definition.RestServiceValidator;
import com.raizlabs.android.broker.compiler.definition.Validator;
import com.raizlabs.android.broker.core.RestService;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RestServiceHandler extends BaseHandler {

    public RestServiceHandler() {
        super(RestService.class, new RestServiceValidator());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onProcessElement(RequestManager requestManager, RoundEnvironment roundEnvironment,
                                    TypeElement element, Validator validator) {
        RestServiceDefinition restServiceDefinition = new RestServiceDefinition(requestManager, element);
        if(validator.validate(requestManager, restServiceDefinition)) {
            try {
                JavaWriter javaWriter = new JavaWriter(requestManager.getProcessingEnvironment().getFiler()
                        .createSourceFile(restServiceDefinition.getSourceFileName()).openWriter());
                restServiceDefinition.write(javaWriter);
                requestManager.addRestServiceDefinition(restServiceDefinition);
            } catch (IOException e) {
                requestManager.logError(e);
            }
        }
    }
}
