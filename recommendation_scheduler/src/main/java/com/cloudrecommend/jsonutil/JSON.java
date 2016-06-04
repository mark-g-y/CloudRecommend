package com.cloudrecommend.jsonutil;

import org.json.JSONException;
import org.json.JSONObject;

// wrapper for JSON operations to avoid try/catch blocks everywhere
public class JSON {

    public static JSONObject create(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject put(JSONObject obj, String key, String value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String getString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getLong(JSONObject obj, String key) {
        try {
            return obj.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
