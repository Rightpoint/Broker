package com.raizlabs.android.broker.responsehandler;

/**
 * This will intercept a response and return the intended type of response that we're expecting.
 * The {@link ResponseType} should match the actual type of the response. The {@link ReturnType} should
 * return what the {@link com.raizlabs.android.broker.RequestCallback} expects.
 */
public interface ResponseHandler<ResponseType, ReturnType> {

    /**
     * Handles the response by converting it from one type to the type that the {@link com.raizlabs.android.broker.RequestCallback} handles;
     * @param responseType
     * @return
     */
    public ReturnType processResponse(ResponseType responseType);
}
