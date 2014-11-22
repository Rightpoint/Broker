package com.raizlabs.android.request.compiler.handler;

import com.raizlabs.android.request.compiler.RequestManager;

import javax.annotation.processing.RoundEnvironment;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public interface Handler {

    public void handle(RequestManager requestManager, RoundEnvironment roundEnvironment);
}
