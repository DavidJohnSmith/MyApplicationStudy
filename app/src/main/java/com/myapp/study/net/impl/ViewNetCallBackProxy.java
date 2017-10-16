package com.myapp.study.net.impl;

import android.os.Parcelable;

import com.myapp.study.BuildConfig;
import com.myapp.study.etc.HttpConfigModel;
import com.myapp.study.net.ConnectTool;
import com.myapp.study.net.interfaces.IViewNetCallBack;
import com.myapp.study.utils.GsonTool;
import com.myapp.study.utils.JsonTool;
import com.myapp.study.utils.MD5;
import com.myapp.study.utils.MapUtil;
import com.myapp.study.utils.PreferenceManager;
import com.myapp.study.utils.ToastUtil;

import java.util.Map;

/**
 * Created by lenovo on 2015/10/30.
 */
public class ViewNetCallBackProxy implements IViewNetCallBack {
    private IViewNetCallBack listener;
    private Class entityClass;
    private HttpConfigModel httpConfigModel;
    private Map<String, Object> param;

    public ViewNetCallBackProxy(IViewNetCallBack listener, Class entityClass, HttpConfigModel httpConfigModel) {
        this.listener = listener;
        this.entityClass = entityClass;
        this.httpConfigModel = httpConfigModel;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }


    @Override
    public void onResponse(Parcelable result, int httpConfigId, boolean fromNet, Object o) {

    }


    @Override
    public void onFail(final Exception e) {
        ConnectTool.handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onFail(e);
            }
        });

    }

    @Override
    public boolean dispatchResult(final String jsonString) {
        if (httpConfigModel.dispatchUI) {
            ConnectTool.handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.dispatchResult(jsonString);
                }
            });
            return true;
        } else {
            try {
                if (httpConfigModel.showToast) {
                    ToastUtil.show(JsonTool.getString(jsonString, "message"));
                }
                if (httpConfigModel.needCache) {
                    String requestUrl = BuildConfig.API_URL + httpConfigModel.action;
                    String tempUrl = MapUtil.mapToUrlParams(requestUrl, param);
                    String key = MD5.md5(tempUrl);
                    PreferenceManager.getInstance().saveValueByKeyFromTable(jsonString, key, "net");
                }

                final Parcelable ser = (Parcelable) GsonTool.jsonToEntity(jsonString, entityClass);
                ConnectTool.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResponse(ser, httpConfigModel.id, true, null);
                    }
                });


            } catch (final Exception e) {
                e.printStackTrace();
                ConnectTool.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFail(e);
                    }
                });
            }
        }
        return false;
    }

}
