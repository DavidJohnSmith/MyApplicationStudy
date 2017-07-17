package com.myapp.study.application;

import android.app.Application;
import android.content.res.AssetManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.myapp.study.log.LogUtil;
import com.myapp.study.net.NetManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MyApplication extends Application {

    public static MyApplication instance;
    private String json = null;
    private String cachePath;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, NetManager.getInstance().getOkHttpClient())
                .build();
        Fresco.initialize(this, config);
    }


    private void initJson() {
        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open("url.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");
        } catch (IOException e) {
            LogUtil.e(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(e.getMessage());
                }
            }

        }
    }

    public String getJson() {
        if (null == json) {
            initJson();
        }
        return json;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        File file = new File(cachePath);
        if (!file.exists()) {
            boolean isSuccess = file.mkdirs();
            LogUtil.e("cachePath create:" + cachePath + isSuccess);
        }
        this.cachePath = cachePath;
    }

}
