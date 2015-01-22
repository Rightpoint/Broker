package com.raizlabs.android.broker.tests;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.core.Endpoint;
import com.raizlabs.android.broker.core.Header;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.Part;
import com.raizlabs.android.broker.core.Priority;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;

import org.json.JSONArray;

/**
 * Description:
 */
@RestService(baseUrl = TestRestInterface2.BASE_URL)
public interface TestRestInterface2 {

    public static final String BASE_URL = "http://jsonplaceholder.typicode.com";

    public static String ALBUMS = "albums";

    public static String COMMENTS = "comments";

    @Method(url = ALBUMS,
            priority = Priority.HIGH,
            params = {@Param(name = "userId", value = "myNumber")})
    public Request<JSONArray> getAllAlbumsRequest(
            @Param("albumId") String albumId,
            RequestCallback<JSONArray> callback);

    @Method(url = COMMENTS,
            priority = Priority.IMMEDIATE,
            headers = {@Header(name = "userId", value = "myNumber")})
    public Request<JSONArray> getAllCommentsRequest(
            @Header("albumId") String albumId,
            RequestCallback<JSONArray> callback);

    @Method(url = "/{endpoint1}/{endpoint2}/",
            headers = {@Header(name = "User-Agent", value = "Android")},
            parts = {@Part(name = "isOpen", value = "true")})
    @ResponseHandler(SimpleJsonArrayResponseHandler.class)
    public Request<JSONArray> getExamplePartRequest(@Part(value = "albumId",
                                                    isFile = true) String albumId,
                                                    @Endpoint String endpoint1,
                                                    @Endpoint String endpoint2);
}
