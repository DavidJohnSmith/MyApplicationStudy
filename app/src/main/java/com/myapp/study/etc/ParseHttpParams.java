package com.myapp.study.etc;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapp.study.application.MyApplication;

import java.util.List;

/**
 * Created by root on 15-5-3.
 */
public class ParseHttpParams {

    private static ParseHttpParams instance = null;

    private static List<HttpConfigModel> configModelList;

    private ParseHttpParams() {

    }

    public static ParseHttpParams getInstance() {
        if (instance == null) {
            synchronized (ParseHttpParams.class) {
                if (null == instance) {
                    instance = new ParseHttpParams();
                    Gson gson = new Gson();
                    configModelList = gson.fromJson(MyApplication.instance.getJson(), new TypeToken<List<HttpConfigModel>>() {
                    }.getType());
                }
            }
        }
        return instance;
    }

    public HttpConfigModel getConfigById(HttpConfig config) {
        return configModelList.get(config.ordinal());
    }
}
