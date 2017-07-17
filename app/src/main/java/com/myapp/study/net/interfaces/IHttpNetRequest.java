package com.myapp.study.net.interfaces;


import com.myapp.study.etc.HttpConfigModel;
import com.myapp.study.net.exception.NetException;

import java.util.Map;

/**
 * Created by lenovo on 2015/4/30.
 */
public interface IHttpNetRequest {

    /**
     * @param url              get请求的url
     * @param params           get请求的参数
     * @param responseCallBack get请求的回调函数
     * @throws NetException
     */
    void get(String url, Map<String, Object> params, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, String tag) throws NetException;


    /**
     * @param url              get请求的url
     * @param params           get请求的参数
     * @param header           get请求的头参数
     * @param responseCallBack get请求的回调函数
     * @throws NetException
     */
    void getWithHeader(String url, Map<String, Object> params, Map<String, String> header,
                       HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, String tag) throws NetException;


    /**
     * @param url              post请求的url
     * @param params           post请求的参数
     * @param responseCallBack post请求的回调函数
     * @throws NetException
     */
    void post(String url, Map<String, Object> params, HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, boolean isForm, String tag) throws NetException;


    /**
     * @param url              post请求的url
     * @param params           post请求的参数
     * @param header           post请求的头参数
     * @param responseCallBack
     */
    void postWithHeader(String url, Map<String, Object> params, Map<String, String> header,
                        HttpConfigModel httpConfigModel, IViewNetCallBack responseCallBack, boolean isForm, String tag) throws NetException;


    /**
     * 取消请求
     *
     * @param tag 请求标记
     */
    void cancel(String tag);
}
