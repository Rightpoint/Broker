package com.raizlabs.android.broker.test;

import com.raizlabs.android.broker.RequestCallback;
import com.raizlabs.android.broker.core.Body;
import com.raizlabs.android.broker.core.Method;
import com.raizlabs.android.broker.core.Param;
import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.raizlabs.android.broker.responsehandler.SimpleJsonResponseHandler;
import com.raizlabs.android.broker.volley.VolleyExecutor;

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
