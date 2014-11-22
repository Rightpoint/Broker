package com.raizlabs.android.request.compiler.handler;

import com.raizlabs.android.request.compiler.RequestManager;
import com.raizlabs.android.request.compiler.definition.RestServiceDefinition;
import com.raizlabs.android.request.core.RestService;
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
        super(RestService.class);
    }

    @Override
    protected void onProcessElement(RequestManager requestManager, RoundEnvironment roundEnvironment, TypeElement element) {
        RestServiceDefinition restServiceDefinition = new RestServiceDefinition(requestManager, element);
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
