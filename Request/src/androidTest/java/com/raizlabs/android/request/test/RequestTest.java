package com.raizlabs.android.request.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;

import com.raizlabs.android.request.Request;
import com.raizlabs.android.request.RequestCallback;
import com.raizlabs.android.request.RequestExecutor;
import com.raizlabs.android.request.RequestUtils;
import com.raizlabs.android.request.SimpleUrlProvider;
import com.raizlabs.android.request.responsehandler.SimpleJsonResponseHandler;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public class RequestTest extends AndroidTestCase {

    private RequestExecutor<Void> mRequestExecutor = new DummyExecutor();

    private RequestCallback<JSONObject> mTestResponse = new RequestCallback<JSONObject>() {
        @Override
        public void onRequestDone(JSONObject jsonObject) {
            assertNotNull(jsonObject);
        }

        @Override
        public void onRequestError(Throwable error, String stringError) {
            assertNull(error);
            assertNull(stringError);
        }
    };

    public void testJsonRequest() {
        assertTrue(getContext().getPackageManager().checkPermission(Manifest.permission.INTERNET, getContext().getPackageName()) == PackageManager.PERMISSION_GRANTED);
        Request<String> stringRequest
                = new Request.Builder<String>(mRequestExecutor)
                .provider(new SimpleUrlProvider("https://mobilecontent.costco.com/live/config/appConfig.json"))
                .responseHandler(new SimpleJsonResponseHandler())
                .execute(mTestResponse);

        assertNotNull(stringRequest.getUrl());
        assertTrue(stringRequest.getMethod().equals(Request.Method.GET));
    }

    public void testUrlParams() {
        Request<String> testRequest
                = new Request.Builder<String>(new RequestExecutor() {
            @Override
            public void execute(Request request) {
            }

            @Override
            public void cancelRequest(Object o, Request request) {
            }

            @Override
            public void cancelAllRequests() {
            }
        })
                .provider(new SimpleUrlProvider("https://mobilecontent.costco.com/"))
                .addUrlParam("test", "this is a test")
                .addUrlParam("hello", "testing")
                .addRequestHeader("header", "test header")
                .execute(null);

        String url = RequestUtils.formatURL(testRequest.getUrl(), testRequest.getParams());

        assertEquals(testRequest.getUrl()+"?test=this+is+a+test&hello=testing", url);
    }


    private class DummyExecutor implements RequestExecutor<Void> {

        private HttpClient mClient = new DefaultHttpClient();

        @Override
        public void execute(final Request request) {
            String url = request.getUrl();
            assertNotNull(url);

            HttpRequestBase requestBase = null;
            if(request.getMethod().equals(Request.Method.GET)) {
                requestBase = new HttpGet(url);
            }

            if(requestBase != null) {
                Map<String, String> headers = request.getHeaders();
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    requestBase.addHeader(key, headers.get(key));
                }

                final HttpRequestBase finalRequestBase = requestBase;
                        try {
                            HttpResponse response = mClient.execute(finalRequestBase);
                            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                response.getEntity().writeTo(out);
                                out.close();
                                String responseString = out.toString();
                                request.getCallback().onRequestDone(request.getResponseHandler().processResponse(responseString));
                            } else{
                                //Closes the connection.
                                response.getEntity().getContent().close();
                                request.getCallback().onRequestError(null, response.getStatusLine().getReasonPhrase());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
            }
        }

        @Override
        public void cancelRequest(Void aVoid, Request request) {

        }

        @Override
        public void cancelAllRequests() {

        }
    }
}
