package com.raizlabs.android.broker.tests;

import android.test.AndroidTestCase;
import android.util.Log;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestManager;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.broker.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.broker.rest.BaseRestInterface;
import com.raizlabs.android.broker.volley.VolleyExecutor;
import com.raizlabs.android.broker.webservicemanager.WebServiceManagerExecutor;
import com.raizlabs.net.webservicemanager.WebServiceManager;
import com.raizlabs.synchronization.OneShotLock;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class TestRestTest extends AndroidTestCase {

    private OneShotLock mRequestLock = new OneShotLock();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RequestConfig.init(getContext(), VolleyExecutor.getSharedExecutor());
    }

    public void testVolley() {
        testBaseRestInterface(RequestManager.getRestInterface(TestRestInterface.class));
    }

    public void testWebServiceManager() {
        TestRestInterface restInterface = RequestManager.getRestInterface(TestWSMInterface.class);
        testBaseRestInterface(restInterface);

        BaseRestInterface baseRestInterface = ((BaseRestInterface) restInterface);
        WebServiceManagerExecutor managerExecutor = ((WebServiceManagerExecutor) baseRestInterface.getRequestExecutor());
        WebServiceManager webServiceManager = managerExecutor.getWebServiceManager();

        // Wait for requests, no timeout specified so if requests fail to remove, we will find it here.
        while (managerExecutor.getActiveRequestSize() != 0) {
            Log.d(getClass().getSimpleName(), "Waiting for requests: " + managerExecutor.getActiveRequestSize());
        }
        assertTrue(managerExecutor.getActiveRequestSize() == 0);
    }

    private void testBaseRestInterface(TestRestInterface restInterface) {
        assertNotNull(restInterface);

        BaseRestInterface baseRestInterface = (BaseRestInterface) restInterface;

        String body = "This Is a Put";
        Request<JSONObject> updateCommentsRequest = restInterface.updateCommentsWithUserId(body, "1", null);
        assertNotNull(updateCommentsRequest.getBody());
        assertEquals(body.length(), updateCommentsRequest.getBodyLength());

        Request<JSONObject> fetchRequest = restInterface.getFetchDataRequest(TestRestInterface.POSTS, "1", TestRestInterface.COMMENTS);
        assertNotNull(fetchRequest.getBaseUrl());
        assertNotNull(fetchRequest.getUrl());
        assertNotNull(fetchRequest.getFullUrl());
        assertEquals(fetchRequest.getFullUrl(), baseRestInterface.getFullBaseUrl() + "/posts/1/comments");
        assertTrue(fetchRequest.getResponseHandler() instanceof SimpleJsonResponseHandler);

        Request.Builder<JSONObject> commentsBuilder = restInterface.getCommentsRequestBuilder();
        Request<JSONObject> commentsRequest = commentsBuilder.build(null);
        assertTrue(commentsRequest.getResponseHandler() instanceof SimpleJsonArrayResponseHandler);
        assertEquals(commentsRequest.getFullUrl(), baseRestInterface.getFullBaseUrl() + "/comments");
        assertTrue(commentsRequest.getMethod() == Method.GET);

        Request<JSONArray> postsRequest = restInterface.getPostsByUserIdParamRequest(1, 3);
        assertEquals(postsRequest.getFullUrl(), baseRestInterface.getFullBaseUrl() + "/comments?userId=1&id=3");

        restInterface.fetchPostsByUserId(1, getArrayReponse());
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchAllComments(getArrayReponse());
        mRequestLock = new OneShotLock();
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchAllPosts(getArrayReponse());
        mRequestLock = new OneShotLock();
        mRequestLock.waitUntilUnlocked();

        restInterface.fetchData(TestRestInterface.POSTS, "1", TestRestInterface.COMMENTS, getArrayReponse());
        mRequestLock = new OneShotLock();
        mRequestLock.waitUntilUnlocked();
    }

    private JsonArrayCallback getArrayReponse() {
        return new JsonArrayCallback() {
            @Override
            public void onRequestDone(JSONArray jsonObject) {
                assertNotNull(jsonObject);
                mRequestLock.unlock();
            }

            @Override
            public void onRequestError(Throwable error, String stringError) {
                assertNull(error);
                assertNull(stringError);
                mRequestLock.unlock();
            }
        };
    }
}
