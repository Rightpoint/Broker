package com.raizlabs.android.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 */
public class RequestUtils {

    private static final String CHARSET_DEF = "UTF-8";

    /**
     * This will format the URL with keys and value params as well as encode the key and values.
     * @param base - the url of the request
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
}
