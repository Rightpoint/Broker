package com.raizlabs.android.broker.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestUtils;

import org.apache.http.HttpEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the basic implementation of using our {@link com.raizlabs.android.broker.Request} library
 */
public class BrokerVolleyRequest<ResponseType> extends com.android.volley.Request<ResponseType> {

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
     * @param errorListener - the listener for an error
     */
    public BrokerVolleyRequest(Request<ResponseType> request, String url, Response.ErrorListener errorListener) {
        super(request.getMethod(), url, errorListener);
        mRequest = request;

        if (mRequest.isMultiPart()) {
            mMultiPartEntity = RequestUtils.createMultipartEntity(request);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Response<ResponseType> parseNetworkResponse(NetworkResponse networkResponse) {

        if (mRequest.hasFile()) {
            InputStream inputStream = new ByteArrayInputStream(networkResponse.data);
            try {
                readContentToFile(mRequest.getDownloadToFile(), inputStream, networkResponse.data.length);
            } catch (IOException e) {
                VolleyLog.e(e, "Error Writing To File");
            }
            return null;
        } else {

            String parsed;
            try {
                parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(networkResponse.data);
            }
            return Response.success((ResponseType) mRequest.getResponseHandler().handleResponse(parsed),
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void deliverResponse(ResponseType response) {
        if (mRequest.getCallback() != null) {
            mRequest.getCallback().onRequestDone(response);
        }
    }

    private boolean readContentToFile(File file, InputStream input, long length) throws IOException {
        // If there was no content stream, fail
        if (input == null) {
            return false;
        }

        // Delete the file if it exists
        if (file.exists()) {
            file.delete();
        }
        // Create the directory for the file
        file.getParentFile().mkdirs();

        FileOutputStream out = null;
        try {
            // Get an output stream to the file
            out = new FileOutputStream(file);
        } catch (IOException e) {
            VolleyLog.v(getClass().getName(), "IOException in readContentToFile", e);
            input.close();
            return false;
        }

        long expectedSize = length;

        try {
            byte[] buffer = new byte[1024];
            long totalRead = 0;
            int read;
            // Pump all the data
            while ((read = input.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                out.flush();
                totalRead += read;
            }
            // If the expected size matches, we succeeded.
            // If the expected size was not defined, we return true as we don't know if it failed.
            return expectedSize == -1 || totalRead == expectedSize;
        } catch (IOException ex) {
            VolleyLog.e(getClass().getName(), "IOException in readContentToFile", ex);
            return false;
        } finally {
            // Close both our streams
            out.close();
            input.close();
        }
    }

    @Override
    public Priority getPriority() {
        return ((VolleyExecutor) mRequest.getExecutor()).convertPriority(mRequest.getPriority());
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