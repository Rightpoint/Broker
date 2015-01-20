package com.raizlabs.android.broker.compiler;

import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RequestUtils {

    public static String getResponseHandler(ResponseHandler annotation) {
        String clazz = null;
        if (annotation != null) {
            try {
                annotation.value();
            } catch (MirroredTypeException mte) {
                clazz = mte.getTypeMirror().toString();
            }
        }
        return clazz;
    }

    public static String getRequestExecutor(RequestExecutor annotation) {
        String clazz = null;
        if (annotation != null) {
            try {
                annotation.value();
            } catch (MirroredTypeException mte) {
                clazz = mte.getTypeMirror().toString();
            }
        }
        return clazz;
    }

    public static String getUrlEncodedString(Map<String, String> params, String url) {
        StringBuilder newUrl = new StringBuilder(url);

        List<String> keySet = new ArrayList<String>(params.keySet());
        if(!keySet.isEmpty()) {
            newUrl.append("?\" + ");
        }
        for(int i = 0; i < keySet.size(); i++) {
            String name = keySet.get(i);
            newUrl.append("\"").append(name).append("=\" + ")
                    .append(params.get(name));

            if(i < keySet.size() - 1) {
                newUrl.append("+ \"&\" + ");
            }
        }

        if(!keySet.isEmpty()) {
            newUrl.append("+ \"");
        }

        return newUrl.toString();
    }

    /**
     * Whether the specified element is assignable to the fqTn parameter
     *
     * @param processingEnvironment The environment this runs in
     * @param fqTn                  THe fully qualified type name of the element we want to check
     * @param element               The element to check that implements
     * @return true if element implements the fqTn
     */
    public static boolean implementsClass(ProcessingEnvironment processingEnvironment, String fqTn, Element element) {
        TypeElement typeElement = processingEnvironment.getElementUtils().getTypeElement(fqTn);
        if (typeElement == null) {
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "Type Element was null for: " + fqTn + "" +
                    "ensure that the visibility of the class is not private.");
            return false;
        } else {
            TypeMirror classMirror = typeElement.asType();
            return processingEnvironment.getTypeUtils().isAssignable(element.asType(), classMirror);
        }
    }
}
