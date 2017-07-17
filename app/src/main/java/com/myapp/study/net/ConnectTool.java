package com.myapp.study.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;

import com.myapp.study.BuildConfig;
import com.myapp.study.etc.HttpConfig;
import com.myapp.study.etc.HttpConfigModel;
import com.myapp.study.etc.ParseHttpParams;
import com.myapp.study.log.LogUtil;
import com.myapp.study.net.impl.BaseNetImpl;
import com.myapp.study.net.interfaces.IHttpNetRequest;
import com.myapp.study.net.interfaces.IViewNetCallBack;
import com.myapp.study.utils.GsonTool;
import com.myapp.study.utils.IConstant;
import com.myapp.study.utils.JsonTool;
import com.myapp.study.utils.MD5;
import com.myapp.study.utils.MapUtil;
import com.myapp.study.utils.NetWorkUtil;
import com.myapp.study.utils.PreferenceManager;
import com.myapp.study.utils.StringTools;

import java.util.Map;

/**
 * 连接底层的 http工具类
 */
public class ConnectTool {

    public static Handler handler = new Handler(Looper.getMainLooper());

    public static void httpRequest(HttpConfig config, Map<String, Object> params, final IViewNetCallBack netCallBack,
                                   Class entityClass, String requestTag) throws Exception {
        httpRequestWithHeader(config, params, null, netCallBack, entityClass, requestTag);
    }

    /**
     * @param config
     * @param param
     * @param header
     * @param netCallBack
     * @param entityClass
     * @param requestTag
     * @throws Exception
     */

    public static void httpRequestWithHeader(HttpConfig config, Map<String, Object> param, Map<String, String> header, final IViewNetCallBack netCallBack,
                                             Class entityClass, String requestTag) throws Exception {
        final HttpConfigModel httpConfigModel = ParseHttpParams.getInstance().getConfigById(config);
        String url = BuildConfig.API_URL + httpConfigModel.action;

        IHttpNetRequest callBack = NetManager.getInstance();
        BaseNetImpl implListener = new BaseNetImpl(netCallBack, entityClass, httpConfigModel);

        /**
         * 需要登录的保存的token
         */
        if (httpConfigModel.needLogin) {
            param.put("token", "");
        }

        implListener.setParam(param);

        /**
         * 如果没有网络并且缓存从缓存中取不走网络
         */
        if (!NetWorkUtil.isNetworkConnected() || requestTag.equals(IConstant.LOAD_CACHE)) {
            if (httpConfigModel.needCache) {
                String cacheUrl = MapUtil.mapToUrlParams(url, param);
                String key = MD5.md5(cacheUrl);
                final String cacheJsonString = PreferenceManager.getInstance().getValueByKeyFromTable(key, "net");
                if (!StringTools.isNullOrEmpty(cacheJsonString)) {
                    if (JsonTool.isArray(cacheJsonString)) {
                        final Parcelable ser = (Parcelable) GsonTool.jsonToArrayEntity(cacheJsonString, entityClass);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                netCallBack.onResponse(ser, httpConfigModel.id, false, cacheJsonString);
                            }
                        });
                        return;
                    }
                    try {
                        final Parcelable ser = (Parcelable) GsonTool.jsonToEntity(cacheJsonString, entityClass);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                netCallBack.onResponse(ser, httpConfigModel.id, false, cacheJsonString);
                            }
                        });

                    } catch (final Exception e) {
                        LogUtil.e(e.getLocalizedMessage());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                netCallBack.onFail(e);
                            }
                        });
                    }
                    return;
                }
            }
        }

        switch (httpConfigModel.method) {
            case IConstant.GET:
                if (httpConfigModel.hasHeader && null != header && header.size() > 0) {
                    callBack.getWithHeader(url, param, header, httpConfigModel, implListener, requestTag);
                    return;
                }
                callBack.get(url, param, httpConfigModel, implListener, requestTag);
                return;
            case IConstant.POST:
                if (httpConfigModel.hasHeader && null != header && header.size() > 0) {
                    callBack.postWithHeader(url, param, header, httpConfigModel, implListener, httpConfigModel.isForm, requestTag);
                    return;
                }
                callBack.post(url, param, httpConfigModel, implListener, httpConfigModel.isForm, requestTag);
                return;
            default:
                break;
        }
    }

    public static void httpRequestCancel(String tag) {
        NetManager.getInstance().cancel(tag);
    }
}
