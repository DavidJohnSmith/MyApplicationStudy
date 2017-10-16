package com.myapp.study.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.myapp.study.BuildConfig;
import com.myapp.study.application.MyApplication;
import com.myapp.study.etc.HttpConfigModel;
import com.myapp.study.log.LogUtil;
import com.myapp.study.net.exception.NetException;
import com.myapp.study.net.interfaces.IHttpNetRequest;
import com.myapp.study.net.interfaces.IViewNetCallBack;
import com.myapp.study.utils.IConstant;
import com.myapp.study.utils.MapUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lenovo on 2015/4/30.
 */
public class NetManager implements IHttpNetRequest {
    private volatile static NetManager instance;
    private static OkHttpClient okHttpClient;

    private NetManager() {

    }

    public static NetManager getInstance() {
        if (instance == null) {
            synchronized (NetManager.class) {
                if (instance == null) {
                    instance = new NetManager();
                    int cacheSize = 10 * 1024 * 1024;
                    Cache cache = new Cache(new File(MyApplication.instance.getCacheDir(), "OkHttpNetCache"), cacheSize);
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.cache(cache)
//                    .cookieJar(new CookiesManager())
                            .connectTimeout(IConstant.commonTimeOut, TimeUnit.SECONDS)
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(@NonNull Chain chain) throws IOException {
                                    Request newRequest = chain.request().newBuilder()
                                            .addHeader("platform", "android")
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            });

                    if (BuildConfig.LOG_DEBUG) {
//                        LogInterceptor logging = new LogInterceptor();
//                        builder.addNetworkInterceptor(logging);
                        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.e("OKHttp3", message);
                            }
                        });
                        //包含header、body数据
                        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        builder.addInterceptor(loggingInterceptor);
                    }
                    okHttpClient = builder.build();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @Override
    public void get(String url, Map<String, Object> params, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, String tag) throws NetException {
        getWithHeader(url, params, null, httpConfigModel, responseCallBack, tag);
    }

    @Override
    public void getWithHeader(String url, Map<String, Object> params, Map<String, String> header, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, String tag) throws NetException {
        url = MapUtil.mapToUrlParams(url, params);
        final Request request = addHeader(header)
                .url(url)
                .tag(tag)
                .build();
        okHttpClient.newCall(request).enqueue(new ResultCallBack(responseCallBack));
    }

    @Override
    public void post(String url, Map<String, Object> params, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, boolean isForm, String tag) throws NetException {
        postWithHeader(url, params, null, httpConfigModel, responseCallBack, isForm, tag);
    }

    @Override
    public void postWithHeader(String url, Map<String, Object> params, Map<String, String> header, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, boolean isForm, String tag) throws NetException {
        RequestBody formBody;
        if (isForm) {
            formBody = addParam(params);
        } else {
            formBody = addParamMultiPart(params);
        }
        final Request request = addHeader(header)
                .url(url)
                .post(formBody)
                .tag(tag)
                .build();
        okHttpClient.newCall(request).enqueue(new ResultCallBack(responseCallBack));
    }

    @Override
    public void cancel(String tag) {
//        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
//            if (tag.equals(call.request().tag())) {
//                call.cancel();
//            }
//        }
//
//        for (Call call : okHttpClient.dispatcher().runningCalls()) {
//            if (tag.equals(call.request().tag())) {
//                call.cancel();
//            }
//        }
    }


    private Request.Builder addHeader(Map<String, String> header) {
        Request.Builder builder = new Request.Builder();
        if (null != header && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }

    private RequestBody addParamMultiPart(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof File) {
                builder.addFormDataPart(entry.getKey(), ((File) entry.getValue()).getName(), RequestBody.create(MediaType.parse("application/octet-stream"), (File) entry.getValue()));
            } else {
                builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
            }

        }
        return builder.build();

    }

    /**
     * 只发送表单数据
     */
    private RequestBody addParam(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return builder.build();
    }


    private static class ResultCallBack implements Callback {
        IViewNetCallBack abstractViewNetCallBack;

        ResultCallBack(IViewNetCallBack responseCallBack) {
            this.abstractViewNetCallBack = responseCallBack;
        }

        @Override
        public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
            try {
                abstractViewNetCallBack.onFail(e);
            } catch (Exception ex) {
                LogUtil.e("exception found" + ex.getCause());
            }
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) {
            try {
                if (response.isSuccessful()) {
                    String body = response.body().string().trim();
                    abstractViewNetCallBack.dispatchResult(body);
                }
            } catch (IOException | NullPointerException e) {
                LogUtil.e("Exception found:" + e.getMessage());
            } finally {
                if (response.body() != null) {
                    response.body().close();
                }

            }
        }
    }

}
