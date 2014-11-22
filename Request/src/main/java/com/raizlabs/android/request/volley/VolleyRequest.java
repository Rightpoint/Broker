package com.raizlabs.android.request.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.raizlabs.android.request.Request;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the basic implementation of using our {@link com.raizlabs.android.request.Request} library
 */
public class VolleyRequest extends StringRequest {

    /**
     * The request that use when certain Volley methods are called.
     */
    private Request mRequest;

    /**
     * Constructs a new volley request with our {@link com.raizlabs.android.request.Request} object
     * @param request - the {@link com.raizlabs.android.request.Request}
     * @param method - the volley Method int
     * @param url - URL formatted url from Request
     * @param listener - the listener for a response
     * @param errorListener - the listener for an error
     */
    public VolleyRequest(Request request, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mRequest = request;
    }

    public VolleyRequest(Request request, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        mRequest = request;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = mRequest.getHeaders();
        if (headers == null)
            headers = new HashMap<String, String>();
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return mRequest.getParams();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Response<String> responseString = super.parseNetworkResponse(response);
        mRequest.getResponseHandler().processResponse(responseString.result);
        return responseString;
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
        try {
            return mRequest.getBody() == null ? super.getBody() : mRequest.getBody().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            return super.getBody();
        }
    }
}