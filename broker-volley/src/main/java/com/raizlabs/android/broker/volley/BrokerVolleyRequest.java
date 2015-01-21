package com.raizlabs.android.broker.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.raizlabs.android.broker.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the basic implementation of using our {@link com.raizlabs.android.broker.Request} library
 */
public class BrokerVolleyRequest extends StringRequest {

    /**
     * The request that use when certain Volley methods are called.
     */
    private Request mRequest;

    /**
     * Constructs a new volley request with our {@link com.raizlabs.android.broker.Request} object
     *
     * @param request       - the {@link com.raizlabs.android.broker.Request}
     * @param method        - the volley Method int
     * @param url           - URL formatted url from Request
     * @param listener      - the listener for a response
     * @param errorListener - the listener for an error
     */
    public BrokerVolleyRequest(Request request, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(request.getMethod(), url, listener, errorListener);
        mRequest = request;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = mRequest.getHeaders();
        if (headers == null)
            headers = new HashMap<>();
        return headers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getParams() {
        return mRequest.getParams();
    }

    @Override
    public String getBodyContentType() {
        String contentType = null;
        if (mRequest.getContentType() != null) {
            contentType = mRequest.getContentType();
        } else {
            contentType = super.getBodyContentType();
        }
        return contentType;
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body;
        if (mRequest.getBody() == null) {
            body = super.getBody();
        } else {

            InputStream requestInputStream = mRequest.getBody();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[(int) mRequest.getBodyLength()];

            try {
                while ((nRead = requestInputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            body = buffer.toByteArray();
        }

        return body;
    }
}