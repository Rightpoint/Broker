package com.raizlabs.android.broker.responsehandler;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Converts a string response into a {@link org.json.JSONArray}
 */
public class SimpleJsonArrayResponseHandler implements ResponseHandler<String, JSONArray> {
    @Override
    public JSONArray handleResponse(String s) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return jsonArray;
        }
    }
}
