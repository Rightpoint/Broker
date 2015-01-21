package com.raizlabs.android.broker.webservicemanager;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestExecutor;
import com.raizlabs.net.HttpMethod;
import com.raizlabs.net.requests.BaseWebServiceRequest;
import com.raizlabs.net.requests.RequestBuilder;
import com.raizlabs.net.requests.WebServiceRequest;
import com.raizlabs.net.responses.Response;
import com.raizlabs.net.webservicemanager.ResultInfo;
import com.raizlabs.net.webservicemanager.WebServiceManager;
import com.raizlabs.net.webservicemanager.WebServiceRequestListener;

/**
 * Description: Executes the requests using our {@link com.raizlabs.net.webservicemanager.WebServiceManager}.
 */
public class WebServiceManagerExecutor implements RequestExecutor<Void> {

    private WebServiceManager mManager = new WebServiceManager();

    public WebServiceManager getWebServiceManager() {
        return mManager;
    }

    @Override
    public void execute(final Request request) {

        final HttpMethod method = WebServiceManagerUtils.convertMethodIntToMethod(request.getMethod());

        WebServiceRequest webServiceRequest = new BaseWebServiceRequest() {
            @Override
            protected RequestBuilder getRequestBuilder() {
                return new RequestBuilder(method, request.getFullUrl());
            }

            @Override
            protected Object translate(Response response) {
                return request.getResponseHandler().processResponse(response.getContentAsString());
            }
        };
        mManager.doRequestInBackground(webServiceRequest, new WebServiceRequestListener() {
            @Override
            public void onRequestComplete(WebServiceManager manager, ResultInfo result) {
                RequestCallback callback = request.getCallback();
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

    @Override
    public void cancelRequest(Void aVoid, Request request) {

    }

    @Override
    public void cancelAllRequests() {

    }
}
