package com.raizlabs.android.request.responsehandler;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Converts a string response into a {@link org.json.JSONArray}
 */
public class SimpleJsonArrayResponseHandler implements ResponseHandler<String, JSONArray> {
    @Override
    public JSONArray processResponse(String s) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
        } catch (JSONException e) {
        } finally {
            return jsonArray;
        }
    }
}
