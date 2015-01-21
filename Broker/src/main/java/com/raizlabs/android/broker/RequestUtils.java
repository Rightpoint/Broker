package com.raizlabs.android.broker;

import com.raizlabs.android.broker.multipart.RequestEntityPart;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description: Provides some handy methods for formatting and encoding urls
 */
public class RequestUtils {

    private static final String CHARSET_DEF = "UTF-8";

    /**
     * This will format the URL with keys and value params as well as encode the key and values.
     *
     * @param base   - the url of the request
     * @param params - the params of the request to put into the URL
     * @return url formatted URL
     */
    public static String formatURL(String base, Map<String, String> params) {
        String url = base;
        if (params != null && params.size() > 0) {
            url += "?";
            Iterator<String> it = params.keySet().iterator();
            if (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                url += tryEncode(key) + "=" + tryEncode(value);
            }
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                url += "&" + tryEncode(key) + "=" + tryEncode(value);
            }
        }
        return url;
    }

    /**
     * Wraps the encoding in an exception block. In the future we will want to turn on or off logging for this.
     *
     * @param data
     * @return
     */
    public static String tryEncode(Object data) {

        String encoded = "";
        try {
            encoded = URLEncoder.encode(String.valueOf(data), CHARSET_DEF);
        } catch (UnsupportedEncodingException e) {
        }

        return encoded;
    }

    /**
     * @param request The request to extract the parts from.
     * @return A an {@link org.apache.http.HttpEntity} that is used in a multipart request.
     */
    @SuppressWarnings("unchecked")
    public static HttpEntity createMultipartEntity(Request request) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        List<RequestEntityPart> entityPartList = request.getParts();
        for (RequestEntityPart part : entityPartList) {
            if (part.isFile()) {
                builder.addBinaryBody(part.getName(), new File(part.getValue()));
            } else {
                builder.addTextBody(part.getName(), part.getValue());
            }
        }
        return builder.build();
    }
}
