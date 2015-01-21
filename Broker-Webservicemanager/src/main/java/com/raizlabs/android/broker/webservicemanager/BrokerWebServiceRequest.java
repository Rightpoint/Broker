package com.raizlabs.android.broker.webservicemanager;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.net.requests.BaseWebServiceRequest;
import com.raizlabs.net.requests.RequestBuilder;
import com.raizlabs.net.responses.Response;
import com.raizlabs.net.webservicemanager.ResultInfo;
import com.raizlabs.net.webservicemanager.WebServiceManager;
import com.raizlabs.net.webservicemanager.WebServiceRequestListener;

/**
 * Description: A simple wrapper that is the mediator between a {@link com.raizlabs.net.requests.BaseWebServiceRequest}
 * and the library's {@link com.raizlabs.android.broker.Request}.
 */
public class BrokerWebServiceRequest<ResultType> extends BaseWebServiceRequest<ResultType> {

    private final RequestBuilder mBuilder;

    private final Request<ResultType> mRequest;

    public BrokerWebServiceRequest(Request<ResultType> request) {
        mBuilder = new RequestBuilder(WebServiceManagerUtils.convertMethodIntToMethod(request.getMethod()),
                request.getFullUrl());
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
        return (ResultType) mRequest.getResponseHandler().handleResponse(response.getContentAsString());
    }

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
