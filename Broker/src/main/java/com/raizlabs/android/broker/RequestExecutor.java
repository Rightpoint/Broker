package com.raizlabs.android.broker;

import com.raizlabs.android.broker.core.Priority;

/**
 * This is the main interface that we need to provide for any third-party request library
 * we use wih our library.
 * <br />
 * The type param {@link RequestMetaData} corresponds to a Tag or piece of information that the
 * executor can pass use to cancel a request. It should match the metadata field in {@link com.raizlabs.android.broker.Request}
 */
public interface RequestExecutor<RequestMetaData> {

    /**
     * Executes the request on this executor. When implemented, it should ensure the request is fully used.
     *
     * @param request
     */
    public void execute(Request request);

    /**
     * Cancels a request
     *
     * @param data    - a tag or some other ID that we use to find the request. Can be null or empty.
     * @param request - the original request (could be null if data is not)
     */
    public void cancelRequest(RequestMetaData data, Request request);

    /**
     * Cancels all pending requests in this executor.
     */
    public void cancelAllRequests();

    /**
     * @param priority
     * @return Conversion into the request executors priority that it can understand.
     */
    public Object convertPriority(Priority priority);
}
