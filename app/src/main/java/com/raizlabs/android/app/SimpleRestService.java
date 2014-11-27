package com.raizlabs.android.app;

import com.raizlabs.android.request.RequestCallback;
import com.raizlabs.android.request.core.Body;
import com.raizlabs.android.request.core.Endpoint;
import com.raizlabs.android.request.core.Header;
import com.raizlabs.android.request.core.Metadata;
import com.raizlabs.android.request.core.Method;
import com.raizlabs.android.request.core.Param;
import com.raizlabs.android.request.core.RequestExecutor;
import com.raizlabs.android.request.core.ResponseHandler;
import com.raizlabs.android.request.core.RestService;
import com.raizlabs.android.request.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.request.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.request.volley.VolleyExecutor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@RestService(baseUrl = "https://www.google.com")
@RequestExecutor(VolleyExecutor.class)
@ResponseHandler(SimpleJsonResponseHandler.class)
public interface SimpleRestService {

    @Method(url = "/users/{userName}/{password}",
            headers = {@Header(name = "MAC", value = "OS")})
    public void getUsers(@Endpoint String password, @Endpoint String userName, @Header("User-Agent") String userAgent, RequestCallback<JSONObject> requestCallback);

    @Method(url = "/users/{firstName}", method = Method.PUT)
    public void putFirstName(@Endpoint String firstName, @Body String requestBody, RequestCallback<JSONObject> requestCallback);

    @Method(url = "/hello/{goodBye}", method = Method.DELETE)
    public void deleteGoodbye(@Param("myNameIs") String what,
                              @Param("jasonSays") String yeah,
                              @Endpoint String goodBye,
                              @Metadata double flack,
                              RequestCallback<JSONObject> requestCallback);

    @Method(url = "/hello/yep")
    @ResponseHandler(SimpleJsonArrayResponseHandler.class)
    public void getYep(RequestCallback<JSONArray> requestCallback);
}
