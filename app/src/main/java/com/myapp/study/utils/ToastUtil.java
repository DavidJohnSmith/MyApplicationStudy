package com.myapp.study.utils;

import android.widget.Toast;

import com.myapp.study.application.MyApplication;

/**
 * Created by lvgy on 16/3/29.
 */
public class ToastUtil {
    /**
     * 提示内容
     *
     * @param toast 内容
     */
    public static void show(String toast) {
        Toast.makeText(MyApplication.instance, toast, Toast.LENGTH_SHORT).show();
    }
}
