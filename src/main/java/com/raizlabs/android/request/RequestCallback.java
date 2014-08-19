package com.raizlabs.android.request;

/**
 * Gets called when the request finishes, either successfully or with an error. Specifies a response
 * type so we can handle it appropriately.
 */
public interface RequestCallback<ResponseType> {

    /**
     * Called when the request was successful and we received a response.
     * @param responseType - the type of response (i.e: a String). It may be null
     */
    public void onRequestDone(ResponseType responseType);

    /**
     * Called when the request fails for some reason.
     * @param error - can be null
     * @param stringError - error represented by a string, which can be null too.
     */
    public void onRequestError(Throwable error, String stringError);
}
