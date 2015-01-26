package com.raizlabs.android.broker.tests.volley;

import android.test.AndroidTestCase;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestManager;
import com.raizlabs.android.broker.tests.TestRestInterface2;
import com.raizlabs.android.broker.volley.BrokerVolleyRequest;

import org.json.JSONArray;


/**
 * Description: Tests the Volley conversion from {@link com.raizlabs.android.broker.Request}
 * into a volley {@link com.android.volley.Request}
 */
public class VolleyRequestTest extends AndroidTestCase {

    public void testVolleyRequest() {

        TestRestInterface2 restInterface = RequestManager.getRestInterface(TestRestInterface2.class);

        Request<JSONArray> request = restInterface.getAllAlbumsAsJSONArray("3", mCallback);
        TestBrokerVolleyRequest<JSONArray> brokerVolleyRequest = new TestBrokerVolleyRequest<>(request, errorListener);

        NetworkResponse networkResponse = new NetworkResponse(("[{" +
                "candy: true," +
                "name: \"andrew\"" +
                "}]").getBytes());
        JSONArray jsonArray = brokerVolleyRequest.getParseResponse(networkResponse).result;
        assertNotNull(jsonArray);
        assertTrue(jsonArray.length() == 1);

        try {
            assertEquals(request.getHeaders(), brokerVolleyRequest.getHeaders());
        } catch (AuthFailureError authFailureError) {
            throw new RuntimeException(authFailureError);
        }

        assertEquals(request.getParams(), brokerVolleyRequest.getParams());
    }

    private static class TestBrokerVolleyRequest<ResponseType> extends BrokerVolleyRequest<ResponseType> {

        /**
         * Constructs a new volley request with our {@link com.raizlabs.android.broker.Request} object
         *
         * @param request       - the {@link com.raizlabs.android.broker.Request}
         * @param errorListener - the listener for an error
         */
        public TestBrokerVolleyRequest(Request<ResponseType> request, Response.ErrorListener errorListener) {
            super(request, errorListener);
        }

        public Response<ResponseType> getParseResponse(NetworkResponse response) {
            return parseNetworkResponse(response);
        }

    }


    private final Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            assertNull(volleyError);
        }
    };

    private final RequestCallback<JSONArray> mCallback = new RequestCallback<JSONArray>() {
        @Override
        public void onRequestDone(JSONArray jsonArray) {
        }

        @Override
        public void onRequestError(Throwable error, String stringError) {
        }
    };
}
