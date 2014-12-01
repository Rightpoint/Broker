package com.raizlabs.android.broker.responsehandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Converts the String Response into a {@link org.json.JSONObject} response.
 */
public class SimpleJsonResponseHandler implements ResponseHandler<String, JSONObject> {
    @Override
    public JSONObject processResponse(String s) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }
    }
}
