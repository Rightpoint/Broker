package com.raizlabs.android.broker.webservicemanager;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestUtils;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.logging.Logger;
import com.raizlabs.net.requests.BaseWebServiceRequest;
import com.raizlabs.net.requests.RequestBuilder;
import com.raizlabs.net.responses.Response;
import com.raizlabs.net.webservicemanager.ResultInfo;
import com.raizlabs.net.webservicemanager.WebServiceManager;
import com.raizlabs.net.webservicemanager.WebServiceRequestListener;

import org.apache.http.HttpEntity;

import java.io.IOException;

/**
 * Description: A simple wrapper that is the mediator between a {@link com.raizlabs.net.requests.BaseWebServiceRequest}
 * and the library's {@link com.raizlabs.android.broker.Request}.
 */
public class BrokerWebServiceRequest<ResultType> extends BaseWebServiceRequest<ResultType> {

    private final RequestBuilder mBuilder;

    private final Request<ResultType> mRequest;

    /**
     * Constructs a new broker webservice request with our {@link com.raizlabs.android.broker.Request} object.
     *
     * @param request The request to wrap and handle the {@link com.raizlabs.net.requests.BaseWebServiceRequest} class.
     */
    public BrokerWebServiceRequest(Request<ResultType> request) {
        mBuilder = new RequestBuilder(WebServiceManagerUtils.convertMethodIntToMethod(request.getMethod()),
                request.getFullUrl());

        if (request.hasBody()) {
            mBuilder.setInputStream(request.getBody(), request.getBodyLength(), null);
        } else if (request.isMultiPart()) {
            HttpEntity multipartEntity = RequestUtils.createMultipartEntity(request);
            try {
                mBuilder.setInputStream(multipartEntity.getContent(), multipartEntity.getContentLength(), null);
            } catch (IOException e) {
                Logger.e(getClass().getSimpleName(), "Error getting content for the multipart request");
            }
        }
        mBuilder.addHeaders(request.getHeaders());
        if (request.getMethod() == Method.PUT
                || request.getMethod() == Method.POST
                || request.getMethod() == Method.PATCH) {
            mBuilder.addHeader(Request.CONTENT_TYPE_HEADER, request.getBodyContentType());
        }
        mBuilder.addParams(request.getParams());
        mRequest = request;
    }

    @Override
    protected RequestBuilder getRequestBuilder() {
        return mBuilder;
    }

    public Request<ResultType> getRequest() {
        return mRequest;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ResultType translate(Response response) {

        // Download content into file specified.
        if (mRequest.hasFile()) {
            response.readContentToFile(mRequest.getDownloadToFile(), null);
            return null;
        } else {
            return (ResultType) mRequest.getResponseHandler().handleResponse(response.getContentAsString());
        }
    }

    /**
     * Executes this request on the specified {@link com.raizlabs.android.broker.webservicemanager.WebServiceManagerExecutor}
     *
     * @param executor The executor to run
     */
    public void execute(final WebServiceManagerExecutor executor) {
        executor.addRequest(this);
        executor.getWebServiceManager().doRequestInBackground(this, new WebServiceRequestListener<ResultType>() {
            @Override
            public void onRequestComplete(WebServiceManager manager, ResultInfo<ResultType> result) {
                RequestCallback<ResultType> callback = mRequest.getCallback();
                executor.removeRequest(BrokerWebServiceRequest.this);
                if (callback != null && !result.wasCancelled()) {
                    if (result.isStatusOK()) {
                        callback.onRequestDone(result.getResult());
                    } else {
                        callback.onRequestError(null, result.getResponseMessage());
                    }
                }
            }
        });
    }
}
