package com.a2dict.equery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by a2dict on 2019/11/10
 */
public class JsonUtil {
    public static List<Object> toJavaList(String json) {
        try {
            return new JSONArray(json)
                    .toList();
        } catch (JSONException | NullPointerException e) {
            return null;
        }
    }
}
