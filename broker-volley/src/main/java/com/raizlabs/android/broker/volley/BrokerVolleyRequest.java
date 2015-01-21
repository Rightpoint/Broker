package com.raizlabs.android.broker.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestUtils;

import org.apache.http.HttpEntity;

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
     * Used if this request contains multipart, we will write out the parts.
     */
    private HttpEntity mMultiPartEntity;

    /**
     * Constructs a new volley request with our {@link com.raizlabs.android.broker.Request} object
     *
     * @param request       - the {@link com.raizlabs.android.broker.Request}
     * @param url           - URL formatted url from Request
     * @param listener      - the listener for a response
     * @param errorListener - the listener for an error
     */
    public BrokerVolleyRequest(Request request, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(request.getMethod(), url, listener, errorListener);
        mRequest = request;

        if (mRequest.isMultiPart()) {
            mMultiPartEntity = RequestUtils.createMultipartEntity(request);
        }
    }

    @Override
    public Priority getPriority() {
        return ((VolleyExecutor)mRequest.getExecutor()).convertPriority(mRequest.getPriority());
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
        String contentType;
        if (mMultiPartEntity != null) {
            contentType = mMultiPartEntity.getContentType().getValue();
        } else if (mRequest.getContentType() != null) {
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

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            if (mMultiPartEntity != null) {
                try {
                    mMultiPartEntity.writeTo(buffer);
                } catch (IOException e) {
                    VolleyLog.e(e, "Error when writing MultiPart entity");
                }
            } else {

                InputStream requestInputStream = mRequest.getBody();
                int nRead;
                byte[] data = new byte[(int) mRequest.getBodyLength()];

                try {
                    while ((nRead = requestInputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                } catch (IOException e) {
                    VolleyLog.e(e, "Error when writing request body");
                }
            }

            body = buffer.toByteArray();
        }

        return body;
    }
}