package com.raizlabs.android.broker.compiler.definition;

import com.squareup.javawriter.JavaWriter;

import java.io.IOException;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public interface Definition {

    public void write(JavaWriter javaWriter) throws IOException;
}
