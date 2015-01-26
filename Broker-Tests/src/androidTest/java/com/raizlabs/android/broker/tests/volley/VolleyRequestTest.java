package com.raizlabs.android.broker.tests.volley;

import android.test.AndroidTestCase;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.RequestCallbackAdapter;
import com.raizlabs.android.broker.RequestManager;
import com.raizlabs.android.broker.tests.TestRestInterface2;
import com.raizlabs.android.broker.volley.BrokerVolleyRequest;
import com.raizlabs.synchronization.OneShotLock;

import org.json.JSONArray;


/**
 * Description: Tests the Volley conversion from {@link com.raizlabs.android.broker.Request}
 * into a volley {@link com.android.volley.Request}
 */
public class VolleyRequestTest extends AndroidTestCase {

    OneShotLock mLock;

    public void testVolleyRequest() {

        TestRestInterface2 restInterface = RequestManager.getRestInterface(TestRestInterface2.class);

        Request<JSONArray> request = restInterface.getAllAlbumsRequest("3", mCallback);
        BrokerVolleyRequest<JSONArray> brokerVolleyRequest = new BrokerVolleyRequest<>(request, errorListener);



        mLock = new OneShotLock();
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


            mLock.unlock();
        }

        @Override
        public void onRequestError(Throwable error, String stringError) {
            assertNull(error);
            assertNull(stringError);
            mLock.unlock();
        }
    };
}
