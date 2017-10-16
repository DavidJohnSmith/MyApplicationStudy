package com.myapp.study.utils;


import com.google.gson.Gson;
import com.myapp.study.log.LogUtil;
import com.myapp.study.net.exception.ResolveException;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * gson的操作工具类
 */
public class GsonTool {

    /**
     * 将 json转化成实体
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToEntity(String json, Class<T> clazz) throws ResolveException {
        try {
            Gson g = new Gson();
            return g.fromJson(json, clazz);
        } catch (Exception e) {
            LogUtil.e(e.getLocalizedMessage(), e);
            throw new ResolveException("Json Resolve Error");
        }
    }


    public static <T> List<T> jsonToArrayEntity(String jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(jsonArray);
            for (int i = 0; i < ja.length(); i++) {
                list.add(jsonToEntity(ja.get(i).toString(), clazz));
            }

        } catch (Exception e) {
            LogUtil.e(e.getLocalizedMessage(), e);
        }
        return list;
    }


    public static List<String> jsonToStringArrayEntity(String jsonArray) {
        List<String> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(jsonArray);
            for (int i = 0; i < ja.length(); i++) {
                list.add(ja.get(i).toString());
            }
        } catch (Exception e) {
            LogUtil.e(e.getLocalizedMessage(), e);
        }
        return list;
    }


    /**
     * 将实体转换成json
     *
     * @param clazz
     * @return
     */
    public static <T> String entityToJson(T clazz) throws ResolveException {
        try {
            Gson g = new Gson();
            return g.toJson(clazz);
        } catch (Exception e) {
            LogUtil.e(e.getLocalizedMessage(), e);
            throw new ResolveException("entity to json error");
        }

    }

    /**
     * 将 json 转换成hasmap
     *
     * @param json
     * @return
     */
    public static HashMap jsonToHas(String json) throws ResolveException {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, HashMap.class);
        } catch (Exception e) {
            LogUtil.e(e.getLocalizedMessage(), e);
            throw new ResolveException("json to HashMap error" + json);
        }

    }

    /**
     * 实体转换成 hasmap
     *
     * @param t
     * @return
     */
    public static <T> HashMap entityToHas(T t) throws ResolveException {
        String json = entityToJson(t);
        return jsonToHas(json);
    }


}
