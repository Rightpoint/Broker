package com.raizlabs.android.broker;

/**
 * Description: Provides a default implementation where the callback does nothing. You can choose to override
 * {@link #onRequestError(Throwable, String)} if you want.
 */
public abstract class RequestCallbackAdapter<ResponseType> implements RequestCallback<ResponseType> {

    @Override
    public abstract void onRequestDone(ResponseType responseType);

    @Override
    public void onRequestError(Throwable error, String stringError) {}
}
