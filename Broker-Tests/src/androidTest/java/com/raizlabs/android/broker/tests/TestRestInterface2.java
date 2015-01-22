package com.raizlabs.android.broker.tests;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.core.Header;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.Priority;
import com.raizlabs.android.broker.core.RestService;

import org.json.JSONArray;

/**
 * Description:
 */
@RestService(baseUrl = "http://jsonplaceholder.typicode.com")
public interface TestRestInterface2 {

    public static String ALBUMS = "albums";

    public static String COMMENTS = "comments";

    @Method(url = ALBUMS,
        priority = Priority.HIGH,
        params = {@Param(name = "userId", value = "myNumber")})
    public Request.Builder<JSONArray> fetchAllAlbums(RequestCallback<JSONArray> callback);

    @Method(url = COMMENTS,
        priority = Priority.IMMEDIATE,
        headers = {@Header(name = "userId", value = "myNumber")})
    public Request.Builder<JSONArray> fetchAllComments(RequestCallback<JSONArray> callback);
}
