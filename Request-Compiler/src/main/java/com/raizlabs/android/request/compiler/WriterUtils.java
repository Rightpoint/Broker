package com.raizlabs.android.request.compiler;

import com.raizlabs.android.request.compiler.definition.Definition;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class WriterUtils {


    public static void emitOverriddenMethod(JavaWriter javaWriter, String returnType, String methodName,
                                     Set<Modifier> modifierSet,
                                     Definition definition, String...parameters) throws IOException {
        javaWriter.emitEmptyLine();
        javaWriter.emitAnnotation(Override.class);
        javaWriter.beginMethod(returnType, methodName, modifierSet, parameters);
        definition.write(javaWriter);
        javaWriter.endMethod();
    }
}
