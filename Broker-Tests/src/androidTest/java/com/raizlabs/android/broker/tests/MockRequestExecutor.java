package com.raizlabs.android.broker.tests;

import com.raizlabs.android.broker.MockRequest;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestExecutor;

/**
 * Description: Provides a very simple mocking interface so we can pretend to return data in tests.
 */
public class MockRequestExecutor implements RequestExecutor<String> {
    @Override
    public void execute(Request request) {
        MockRequest mockRequest = ((MockRequest) request);
        RequestCallback callback = mockRequest.getCallback();
        Object dummyResponse = mockRequest.getDummyResponse();
        if(callback != null) {
            if(mockRequest.hasError()) {
                callback.onRequestError(null, String.valueOf(dummyResponse));
            } else {
                callback.onRequestDone(mockRequest.getResponseHandler().processResponse(mockRequest.getDummyResponse()));
            }
        }
    }

    @Override
    public void cancelRequest(String s, Request request) {
        // Ignored
    }

    @Override
    public void cancelAllRequests() {
        // Ignored
    }
}
