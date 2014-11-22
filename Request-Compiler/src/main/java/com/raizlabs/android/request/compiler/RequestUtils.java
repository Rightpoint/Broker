package com.raizlabs.android.request.compiler;

import com.raizlabs.android.request.core.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.MirroredTypeException;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RequestUtils {

    public static String getResponseHandler(RestService annotation) {
        String clazz = null;
        if (annotation != null) {
            try {
                annotation.responseHandler();
            } catch (MirroredTypeException mte) {
                clazz = mte.getTypeMirror().toString();
            }
        }
        return clazz;
    }

    public static String getRequestExecutor(RestService annotation) {
        String clazz = null;
        if (annotation != null) {
            try {
                annotation.requestExecutor();
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
}
