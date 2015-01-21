package com.raizlabs.android.broker;

import com.raizlabs.android.broker.metadata.RequestMetadataGenerator;
import com.raizlabs.android.broker.multipart.RequestEntityPart;
import com.raizlabs.android.broker.responsehandler.ResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The class that handles all requests. It simplifies the request set up process and enables swapping out
 * {@link com.raizlabs.android.broker.RequestExecutor} so that we can do limited work to switch networking libraries.
 * This class is not final, but the methods contained are not public and all requests should be done via the
 * {@link com.raizlabs.android.broker.Request.Builder} class.
 */
public class Request<ResponseType> implements UrlProvider {

    /**
     * The {@link com.raizlabs.android.broker.UrlProvider} that we use to retrieve the url for this request.
     */
    private UrlProvider mProvider;

    /**
     * The full url for this request, containing the url from the {@link com.raizlabs.android.broker.UrlProvider}
     * and the encoded URL params.
     */
    private String mFullUrl;

    /**
     * The standard interface of executing requests.
     */
    private RequestExecutor mExecutor;

    /**
     * This will be called when the request has finished.
     */
    private RequestCallback mCallback;

    /**
     * The content-type of the actual request.
     */
    private String mContentType = "application/x-www-form-urlencoded";

    /**
     * A tag or metadata that ID's this request.
     */
    private Object mMetaData;

    /**
     * An optional body such as JSON or String that we put in the request.
     */
    private InputStream mBody;

    /**
     * The length of the body to read
     */
    private long mBodyLength;

    /**
     * Handles responses for us. The default is to do nothing but return the request.
     */
    private ResponseHandler<ResponseType, ?> mResponseHandler = new ResponseHandler<ResponseType, Object>() {
        @Override
        public Object handleResponse(ResponseType o) {
            return o;
        }
    };

    /**
     * The URL-encoded params of a {@link com.raizlabs.android.broker.core.Method#GET} request
     */
    private final Map<String, String> mParams = new LinkedHashMap<>();

    /**
     * The headers that get put into the request.
     */
    private final Map<String, String> mHeaders = new LinkedHashMap<>();

    private final Map<String, RequestEntityPart> mPartMap = new LinkedHashMap<>();

    /**
     * @param requestExecutor
     */
    Request(RequestExecutor requestExecutor) {
        mExecutor = requestExecutor;
    }

    /**
     * Sets a url for this request using an {@link com.raizlabs.android.broker.UrlProvider}
     *
     * @param urlProvider
     */
    void setUrlProvider(UrlProvider urlProvider) {
        mProvider = urlProvider;
    }

    /**
     * Sets the listener for when the request has finished. It will return the response.
     *
     * @param callback
     */
    void setCallback(RequestCallback callback) {
        mCallback = callback;
    }

    /**
     * Sets the object that will convert the response into the appropriate type in the callback.
     *
     * @param responseHandler
     */
    void setResponseHandler(ResponseHandler<ResponseType, ?> responseHandler) {
        mResponseHandler = responseHandler;
    }

    /**
     * Attach meta data that you want to pass into the {@link com.raizlabs.android.broker.RequestExecutor}
     * to use such as a tag or unique id for the request.
     *
     * @param metaData
     */
    void setMetaData(Object metaData) {
        mMetaData = metaData;
    }

    /**
     * Sets the content type of the request
     *
     * @param mContentType
     */
    void setContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    /**
     * Sets the body of the request
     *
     * @param mBody
     */
    void setBody(InputStream mBody, long inputStreamLength) {
        this.mBody = mBody;
        this.mBodyLength = inputStreamLength;
    }

    /**
     * These are URL parameters. Encoding will happen at execution time.
     *
     * @param params
     */
    void putAllParams(Map<String, String> params) {
        params.putAll(params);
    }

    /**
     * Add headers to the request
     *
     * @param headers
     */
    void putAllHeaders(Map<String, String> headers) {
        headers.putAll(headers);
    }


    @Override
    public String getBaseUrl() {
        return mProvider.getBaseUrl();
    }

    /**
     * Gets the url for this request.
     *
     * @return
     */
    @Override
    public String getUrl() {
        if (mProvider == null) {
            throw new RuntimeException("A Request UrlProvider must be defined before running a request");
        }
        String providerUrl = mProvider.getUrl();
        String providerBaseUrl = mProvider.getBaseUrl();
        return providerBaseUrl != null ? (providerBaseUrl + providerUrl) : (providerUrl);
    }

    /**
     * @return The full URL including base url, url, and encoded URL params.
     */
    public String getFullUrl() {
        if (mFullUrl == null) {
            mFullUrl = RequestUtils.formatURL(getUrl(), mParams);
        }

        return mFullUrl;
    }

    @Override
    public int getMethod() {
        return mProvider.getMethod();
    }

    /**
     * @return The request executor for this request.
     */
    public RequestExecutor getExecutor() {
        return mExecutor;
    }

    /**
     * @return The map of parameter key and values for this request.
     */
    public Map<String, String> getParams() {
        return mParams;
    }

    /**
     * @return The map of key and values for headers for this request.
     */
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * @return The callback for this request.
     */
    public RequestCallback<ResponseType> getCallback() {
        return mCallback;
    }

    /**
     * @return Handles this response and converts it into what the {@link com.raizlabs.android.broker.RequestCallback} expects.
     */
    public ResponseHandler getResponseHandler() {
        return mResponseHandler;
    }

    /**
     * @return Data attached to this request to uniquely identify it.
     */
    public Object getMetaData() {
        return mMetaData;
    }

    /**
     * @return The type of content for this request.
     */
    public String getContentType() {
        return mContentType;
    }

    /**
     * @return A input into the body that this request uses.
     */
    public InputStream getBody() {
        return mBody;
    }

    /**
     * @return The length of the body, in bytes.
     */
    public long getBodyLength() {
        return mBodyLength;
    }

    /**
     * @return List of parts that this request contains.
     */
    public List<RequestEntityPart> getParts() {
        return new LinkedList<>(mPartMap.values());
    }

    /**
     * @return True if this request has parts defined for it.
     */
    public boolean isMultiPart() {
        return !mPartMap.isEmpty();
    }

    /**
     * @return True if the length of the body is larger than 0 and an input stream for the body is defined.
     */
    public boolean hasBody() {
        return mBodyLength > 0 && mBody != null;
    }

    /**
     * Runs this request on the defined {@link com.raizlabs.android.broker.RequestExecutor}
     */
    public void execute() {
        if (mExecutor != null) {
            mExecutor.execute(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder retString = new StringBuilder("URL: ").append(getFullUrl());
        retString.append("\nHeaders: ");

        Set<String> headers = mHeaders.keySet();
        for (String header : headers) {
            retString.append(header).append(": ").append(mHeaders.get(header)).append("\t\n");
        }

        if (mBody != null) {
            retString.append("\nBody: ").append(mBody);
        }

        retString.append("\nContent-Type: ").append(mContentType);

        if (mMetaData != null) {
            retString.append("\nMetadata: ").append(mMetaData);
        }

        return retString.toString();
    }

    /**
     * Constructs a {@link com.raizlabs.android.broker.Request} that's run on a {@link com.raizlabs.android.broker.RequestExecutor}.
     * This enables easy request constructing and execution.
     * <br />
     * Note: The RequestCallback responseType and ResponseHandler returnType params should be the same
     * <p/>
     * <br />
     * ResponseType is the type of the expected response. Leave this as {@link java.lang.Object}
     * if you expect different kind of responses for the same request (if that ever happens, something is direly wrong!).
     */
    public static class Builder<ResponseType> {

        protected Request<ResponseType> mRequest;

        protected Builder() {
        }

        /**
         * Contructs the contained {@link com.raizlabs.android.broker.Request} with a
         * {@link com.raizlabs.android.broker.RequestExecutor} to run this request on.
         *
         * @param requestExecutor
         */
        public Builder(RequestExecutor requestExecutor) {
            mRequest = new Request<>(requestExecutor);
        }

        /**
         * Define what {@link com.raizlabs.android.broker.UrlProvider} this request uses.
         *
         * @param urlProvider
         * @return
         */
        public Builder<ResponseType> provider(UrlProvider urlProvider) {
            mRequest.setUrlProvider(urlProvider);
            return this;
        }

        /**
         * Define how to handle the response
         *
         * @param responseHandler
         * @return
         */
        public Builder<ResponseType> responseHandler(ResponseHandler<ResponseType, ?> responseHandler) {
            mRequest.setResponseHandler(responseHandler);
            return this;
        }

        /**
         * Attach a unique ID to this request that the {@link com.raizlabs.android.broker.RequestExecutor}
         * can handle.
         *
         * @param metaData
         * @return
         */
        public Builder<ResponseType> metaData(Object metaData) {
            mRequest.setMetaData(metaData);
            return this;
        }

        /**
         * Attach a unique ID to this request that the {@link com.raizlabs.android.broker.RequestExecutor}
         * can handle using a {@link com.raizlabs.android.broker.metadata.RequestMetadataGenerator}.
         * Call this right before execute, so that the parameters of the contained request are all available.
         *
         * @param generator
         * @return
         */
        public Builder<ResponseType> metaDataGenerator(RequestMetadataGenerator generator) {
            mRequest.setMetaData(generator.generate(mRequest));
            return this;
        }

        /**
         * Sets the contentType header of this request
         *
         * @param contentType
         * @return
         */
        public Builder<ResponseType> contentType(String contentType) {
            mRequest.setContentType(contentType);
            return this;
        }

        /**
         * Optional data passed into the request as byte code.
         *
         * @param body
         * @return
         */
        public Builder<ResponseType> body(String body) {
            byte[] bodyBytes = body.getBytes();
            mRequest.setBody(new ByteArrayInputStream(bodyBytes), bodyBytes.length);
            return this;
        }

        /**
         * Optional data passed into the request as byte code.
         *
         * @param body
         * @return
         */
        public Builder<ResponseType> body(InputStream body, long inputStreamLength) {
            mRequest.setBody(body, inputStreamLength);
            return this;
        }

        /**
         * Optional data passed into the request as byte code.
         *
         * @param body
         * @return
         */
        public Builder<ResponseType> body(File body) {
            try {
                mRequest.setBody(new FileInputStream(body), body.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * Adds a URL param to the request.
         *
         * @param key
         * @param value
         * @return
         */
        public Builder<ResponseType> addUrlParam(String key, String value) {
            mRequest.mParams.put(key, value);
            return this;
        }

        /**
         * Adds a {@link java.util.Map} of key value pairs to be URL formatted.
         *
         * @param map
         * @return
         */
        public Builder<ResponseType> addUrlParams(Map<String, String> map) {
            mRequest.putAllParams(map);
            return this;
        }

        /**
         * Adds a header to this request.
         *
         * @param key
         * @param value
         * @return
         */
        public Builder<ResponseType> addRequestHeader(String key, String value) {
            mRequest.mHeaders.put(key, value);
            return this;
        }

        /**
         * Adds a {@link java.util.Map} of headers to this request.
         *
         * @param map
         * @return
         */
        public Builder<ResponseType> addRequestHeaders(Map<String, String> map) {
            mRequest.putAllHeaders(map);
            return this;
        }

        /**
         * Adds a part for a corresponding multipart request, does not set the part as a file.
         *
         * @param name  The name of the part
         * @param value The value of the part.
         * @return
         */
        public Builder<ResponseType> addPart(String name, String value) {
            return addPart(new RequestEntityPart(name, value, false));
        }

        /**
         * Adds a file part for a corresponding multipart.
         *
         * @param name       The name of the part
         * @param pathToFile The path to the file
         * @return
         */
        public Builder<ResponseType> addFilePart(String name, String pathToFile) {
            return addPart(new RequestEntityPart(name, pathToFile, true));
        }

        /**
         * Adds a {@link com.raizlabs.android.broker.multipart.RequestEntityPart} to this request.
         *
         * @param part The part to add.
         * @return
         */
        public Builder<ResponseType> addPart(RequestEntityPart part) {
            mRequest.mPartMap.put(part.getName(), part);
            return this;
        }

        /**
         * Executes this request, returning on the {@link RequestCallback},
         *
         * @param requestCallback
         * @return
         */
        public Request<ResponseType> build(RequestCallback requestCallback) {
            mRequest.setCallback(requestCallback);
            return mRequest;
        }
    }

}
