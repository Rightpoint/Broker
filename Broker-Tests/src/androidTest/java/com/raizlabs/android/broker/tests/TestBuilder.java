package com.raizlabs.android.broker.tests;

import android.test.AndroidTestCase;

import com.raizlabs.android.broker.Request;
import com.raizlabs.android.broker.RequestConfig;
import com.raizlabs.android.broker.RequestUtils;
import com.raizlabs.android.broker.SimpleUrlProvider;
import com.raizlabs.android.broker.metadata.RequestMetadataGenerator;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 */
public class TestBuilder extends AndroidTestCase {

    static final String BODY_STRING = "This is my test";

    static final String TEST_STRING = "This is a good test.";

    static final String URL = "http://www.google.com";

    public void testBuilder() {
        Request.Builder<String> builder = new Request.Builder<>();
        builder.body(BODY_STRING);
        builder.provider(new SimpleUrlProvider(URL));
        builder.metaDataGenerator(mMetadataGenerator);

        Request<String> request = builder.build();
        assertEquals(RequestConfig.getSharedExecutor(), request.getExecutor());

        assertEquals(request.getBaseUrl() + "/silly", request.getMetaData());

        assertEquals(BODY_STRING, RequestUtils.readRequestBodyIntoString(request));

        try {
            InputStream inputStream = getContext().getAssets().open("test.txt");
            builder.body(inputStream, inputStream.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        request = builder.build();

        assertEquals(TEST_STRING, RequestUtils.readRequestBodyIntoString(request));

        builder.addUrlParam("this", "a test");
        builder.addUrlParam("wellp", "a another test");
        request = builder.build();
        String testUrl = URL + "?this=a+test&wellp=a+another+test";
        assertEquals(testUrl, request.getFullUrl());


    }

    private RequestMetadataGenerator mMetadataGenerator = new RequestMetadataGenerator() {
        @Override
        public Object generate(Request request) {
            return request.getBaseUrl() + "/silly";
        }
    };
}
