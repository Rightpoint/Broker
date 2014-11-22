package com.raizlabs.android.app;

import com.raizlabs.android.request.RequestCallback;
import com.raizlabs.android.request.core.Body;
import com.raizlabs.android.request.core.Endpoint;
import com.raizlabs.android.request.core.Header;
import com.raizlabs.android.request.core.MetaData;
import com.raizlabs.android.request.core.Method;
import com.raizlabs.android.request.core.Param;
import com.raizlabs.android.request.core.RestService;
import com.raizlabs.android.request.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.request.volley.VolleyExecutor;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@RestService(responseHandler = SimpleJsonResponseHandler.class,
        requestExecutor = VolleyExecutor.class,
        baseUrl = "https://www.google.com")
public interface SimpleRestService {

    @Method(url = "/users/{userName}/{password}",
            headers = {@Header(value = "MAC", name = "OS")})
    public void getUsers(@Endpoint String password, @Endpoint String userName, @Header("User-Agent") String userAgent, RequestCallback requestCallback);

    @Method(url = "/users/{firstName}", method = Method.PUT)
    public void putFirstName(@Endpoint String firstName, @Body String requestBody, RequestCallback requestCallback);

    @Method(url = "/hello/{goodBye}", method = Method.DELETE)
    public void deleteGoodbye(@Param("myNameIs") String what,
                              @Param("jasonSays") String yeah,
                              @Endpoint String goodBye,
                              @MetaData double flack,
                              RequestCallback requestCallback);
}
