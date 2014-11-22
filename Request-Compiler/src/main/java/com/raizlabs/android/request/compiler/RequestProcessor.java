package com.raizlabs.android.request.compiler;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import com.raizlabs.android.request.compiler.handler.Handler;
import com.raizlabs.android.request.compiler.handler.RestServiceHandler;
import com.raizlabs.android.request.core.Body;
import com.raizlabs.android.request.core.Endpoint;
import com.raizlabs.android.request.core.Header;
import com.raizlabs.android.request.core.Headers;
import com.raizlabs.android.request.core.Method;
import com.raizlabs.android.request.core.Param;
import com.raizlabs.android.request.core.RestService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description: Handles all annotations and generates code for the app to use.
 */
@AutoService(Processor.class)
public class RequestProcessor extends AbstractProcessor {

    private Handler[] mHandlers = {new RestServiceHandler()};

    private RequestManager requestManager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        requestManager = new RequestManager(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.newHashSet(Method.class.getName(), Headers.class.getName(),
                RestService.class.getName(), Header.class.getName(),
                Body.class.getName(), Param.class.getName(), Endpoint.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(Handler handler: mHandlers) {
            handler.handle(requestManager, roundEnv);
        }
        return false;
    }
}
