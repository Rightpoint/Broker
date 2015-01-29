package com.raizlabs.android.broker.tests;

import com.raizlabs.android.broker.core.RequestExecutor;
import com.raizlabs.android.broker.core.ResponseHandler;
import com.raizlabs.android.broker.core.RestService;
import com.raizlabs.android.broker.responsehandler.SimpleJsonArrayResponseHandler;
import com.raizlabs.android.broker.webservicemanager.WebServiceManagerExecutor;

/**
 * Description: Tests our requests using webservice manager
 */
@RequestExecutor(WebServiceManagerExecutor.class)
@RestService(baseUrl = "http://jsonplaceholder.typicode.com")
@ResponseHandler(SimpleJsonArrayResponseHandler.class)
public interface TestWSMInterface extends TestRestInterface {

}
