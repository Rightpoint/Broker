package com.raizlabs.android.request.test;

import android.test.AndroidTestCase;

import com.raizlabs.android.request.RequestCallback;
import com.raizlabs.android.request.RequestConfig;
import com.raizlabs.android.request.RequestManager;

import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class TestRestTest extends AndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RequestConfig.init(getContext());
    }

    public void testRestInterface() {
        TestRestInterface restInterface = RequestManager.getRestInterface(TestRestInterface.class);
        assertNotNull(restInterface);

        restInterface.getWeather(35, 139, "", new RequestCallback<JSONObject>() {
            @Override
            public void onRequestDone(JSONObject jsonObject) {
                assertNotNull(jsonObject);
            }

            @Override
            public void onRequestError(Throwable error, String stringError) {
                assertNull(error);
                assertNull(stringError);
            }
        });
    }
}
