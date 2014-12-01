package com.raizlabs.android.app;

import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.core.Body;
import com.raizlabs.android.broker.core.Endpoint;
import com.raizlabs.android.broker.core.Header;
import com.raizlabs.android.broker.core.Metadata;
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
