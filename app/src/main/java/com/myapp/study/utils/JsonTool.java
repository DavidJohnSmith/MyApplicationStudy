package com.myapp.study.utils;


import org.json.JSONObject;

public class JsonTool {

    /**
     * 从json 格式中解析 int值
     *
     * @param json json字符串
     * @param key  需要的key
     * @return 返回int值
     */
    public static int getInt(String json, String key) {
        try {
            JSONObject jo = new JSONObject(json);

            return jo.optInt(key, -1);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getString(String json, String key) {
        try {
            JSONObject jo = new JSONObject(json);
            return jo.optString(key, "");
        } catch (Exception e) {
            return "";
        }

    }

    public static boolean getBoolean(String json, String key) {
        try {
            JSONObject jo = new JSONObject(json);

            return jo.getBoolean(key);
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean hasKey(String json, String key) {
        try {
            JSONObject jo = new JSONObject(json);

            return jo.has(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONObject getJson(String json, String key) {
        try {
            JSONObject jo = new JSONObject(json);
            return jo.getJSONObject(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是不是json 数组
     *
     * @param json
     * @return
     */
    public static boolean isArray(String json) {
        return json.startsWith("[");
    }

}
