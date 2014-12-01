package com.raizlabs.android.broker.compiler.handler;

import com.raizlabs.android.broker.compiler.RequestManager;

import javax.annotation.processing.RoundEnvironment;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public interface Handler {

    public void handle(RequestManager requestManager, RoundEnvironment roundEnvironment);
}
