package com.raizlabs.android.broker.test;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.core.Body;
import com.raizlabs.android.broker.core.Endpoint;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.broker.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.broker.volley.VolleyExecutor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@RestService(baseUrl = "http://jsonplaceholder.typicode.com")
@ResponseHandler(SimpleJsonArrayResponseHandler.class)
@RequestExecutor(VolleyExecutor.class)
public interface TestRestInterface  {

    public static final String POSTS = "/posts";

    public static final String COMMENTS = "/comments";


    @Method(url = POSTS)
    public void fetchPostsByUserId(@Param("userId") long userID,
                                   RequestCallback<JSONArray> requestCallback);

    @Method(url = POSTS)
    public void fetchAllPosts(JsonArrayCallback requestCallback);

    @Method(url = COMMENTS)
    public void fetchAllComments(JsonArrayCallback callback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    public void fetchData(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel);

    @Method(url = POSTS + "/{userId}", method = Method.PUT)
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public void updateCommentsWithUserId(@Body String putData, @Endpoint String userId, RequestCallback<JSONObject> requestCallback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public Request<JSONObject> getFetchDataRequest(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel);

    @Method(url = COMMENTS)
    public Request.Builder<JSONObject> getCommentsRequestBuilder();
}
