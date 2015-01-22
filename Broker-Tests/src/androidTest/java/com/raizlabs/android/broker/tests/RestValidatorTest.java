package com.raizlabs.android.broker.tests;

import android.test.AndroidTestCase;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestManager;
import com.raizlabs.android.broker.core.Priority;
import com.raizlabs.android.broker.multipart.RequestEntityPart;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.broker.volley.VolleyExecutor;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Description:
 */
public class RestValidatorTest extends AndroidTestCase {

    public void testRestInterface() {
        TestRestInterface2 restInterface = RequestManager.getRestInterface(TestRestInterface2.class);

        // Testing albums request

        Request<JSONArray> request = restInterface.getAllAlbumsRequest(null);

        assertEquals(request.getResponseHandler(), RequestConfig.getSharedResponseHandler());

        assertEquals(request.getExecutor(), RequestConfig.getSharedExecutor());

        assertEquals(TestRestInterface2.BASE_URL + "/" + TestRestInterface2.ALBUMS, request.getUrl());

        assertEquals(TestRestInterface2.BASE_URL, request.getBaseUrl());

        assertEquals(Priority.HIGH, request.getPriority());

        Map<String, String> params = request.getParams();
        assertTrue(!params.isEmpty());
        String param = params.get("userId");
        assertNotNull(param);
        assertEquals("myNumber", param);

        // Testing headers request

        request = restInterface.getAllCommentsRequest(null);

        assertEquals(request.getResponseHandler(), RequestConfig.getSharedResponseHandler());

        assertEquals(request.getExecutor(), RequestConfig.getSharedExecutor());

        assertEquals(TestRestInterface2.BASE_URL + "/" + TestRestInterface2.COMMENTS, request.getUrl());

        assertEquals(TestRestInterface2.BASE_URL, request.getBaseUrl());

        assertEquals(Priority.IMMEDIATE, request.getPriority());

        Map<String, String> headers = request.getHeaders();
        assertTrue(!headers.isEmpty());
        String header = headers.get("userId");
        assertNotNull(header);
        assertEquals("myNumber", param);

        // Testing part request

        request = restInterface.getExamplePartRequest("test", "test2");

        assertTrue(request.getResponseHandler() instanceof SimpleJsonArrayResponseHandler);

        assertTrue(request.getExecutor() instanceof VolleyExecutor);

        assertEquals(TestRestInterface2.BASE_URL + "/test/test2", request.getUrl());

        assertEquals(TestRestInterface2.BASE_URL, request.getBaseUrl());

        assertEquals(Priority.NORMAL, request.getPriority());

        headers = request.getHeaders();
        assertTrue(!headers.isEmpty());
        String tempHeader = headers.get("User-Agent");
        assertNotNull(tempHeader);
        assertEquals("Android", param);

        List<RequestEntityPart> parts = request.getParts();
        assertTrue(parts.size() == 1);
        RequestEntityPart part = parts.get(0);
        assertEquals("isOpen", part.getName());
        assertEquals("true", part.getValue());
        assertFalse(part.isFile());
    }
}
