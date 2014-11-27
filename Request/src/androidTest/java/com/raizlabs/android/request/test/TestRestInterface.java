package com.raizlabs.android.request.test;

import com.raizlabs.android.request.RequestCallback;
import com.raizlabs.android.request.core.Body;
import com.raizlabs.android.request.core.Method;
import com.raizlabs.android.request.core.Param;
import com.raizlabs.android.request.core.RequestExecutor;
import com.raizlabs.android.request.core.ResponseHandler;
import com.raizlabs.android.request.core.RestService;
import com.raizlabs.android.request.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.request.volley.VolleyExecutor;

import org.json.JSONObject;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
@RestService(baseUrl = "http://api.openweathermap.org/data/2.5")
@ResponseHandler(SimpleJsonResponseHandler.class)
@RequestExecutor(VolleyExecutor.class)
public interface TestRestInterface  {


    @Method(url = "/weather")
    public void getWeather(@Param("lat") double lat, @Param(value = "lon", encode = false) double lon,
                           @Body String body, RequestCallback<JSONObject> requestCallback);
}
