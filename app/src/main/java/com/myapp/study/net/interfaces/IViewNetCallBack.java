package com.myapp.study.net.interfaces;

import android.os.Parcelable;

/**
 * Created by lenovo on 2015/4/30.
 */
public interface IViewNetCallBack {

    void onFail(Exception e);

    void onResponse(Parcelable result, int httpConfigId, boolean fromNet, Object o);

    boolean dispatchResult(String result);

}
