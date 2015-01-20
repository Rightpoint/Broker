package com.raizlabs.android.broker;

/**
 * Description:
 */
public class MockRequest<ResponseType> extends Request<ResponseType> {

    private ResponseType dummyResponse;

    private boolean hasError;

    /**
     * @param requestExecutor
     */
    MockRequest(RequestExecutor requestExecutor) {
        super(requestExecutor);
    }


    void setDummyResponse(ResponseType dummyResponse) {
        this.dummyResponse = dummyResponse;
    }

    public ResponseType getDummyResponse() {
        return dummyResponse;
    }

    public boolean hasError() {
        return hasError;
    }

    void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public static class Builder<ResponseType> extends Request.Builder<ResponseType> {

        protected MockRequest<ResponseType> getMockRequest() {
            return (MockRequest<ResponseType>)mRequest;
        }

        /**
         * Contructs the contained {@link com.raizlabs.android.broker.Request} with a
         * {@link com.raizlabs.android.broker.RequestExecutor} to run this request on.
         *
         * @param requestExecutor
         */
        public Builder(RequestExecutor requestExecutor) {
            mRequest = new MockRequest<>(requestExecutor);
        }

        public Builder<ResponseType> dummyResponse(ResponseType responseType) {
            getMockRequest().setDummyResponse(responseType);
            return this;
        }

        public Builder<ResponseType> setHasError(boolean hasError) {
            getMockRequest().setHasError(hasError);
            return this;
        }

    }
}
