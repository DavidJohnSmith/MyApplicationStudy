package com.myapp.study.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.myapp.study.application.MyApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Preference管理类
 */
public class PreferenceManager {
    private static final String SYSTEM_CACHE = "SYSTEM_CACHE";
    private static PreferenceManager instance;

    private PreferenceManager() {
    }

    public static PreferenceManager getInstance() {
        if (instance == null) {
            instance = new PreferenceManager();
        }
        return instance;
    }


    /**
     * 用sdk的缓存路径  SYSTEM_CACHE 文件中
     *
     * @param value 需要储存的value
     * @param key   需要 储存的key
     */
    public void saveValueByKey(String key, String value) {

        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveValueByKey(String key, int value) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveValueByKey(String key, Boolean bool) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, bool);
        editor.commit();
    }

    public void saveValueByKey(String key, long l) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putLong(key, l);
        editor.commit();
    }

    /**
     * 清除sdk SYSTEM_CACHE 中数据
     */
    public void clearTable() {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 取值 SYSTEM_CACHE  中的value
     *
     * @param key
     * @return value
     */
    public String getValueByKey(String key) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public int getIntValueByKey(String key) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public Boolean getBooleanByKey(String key) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, true);
    }

    public long getLongByKey(String key) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getLong(key, -1);
    }

    /**
     * 用sdk的缓存路径  自定义文件名称 文件中
     *
     * @param value     需要储存的value
     * @param key       需要 储存的key
     * @param tableName 需要 创建的表名称
     */
    public void saveValueByKeyFromTable(String value, String key, String tableName) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 清除sdk 自定义文件名称 中数据
     *
     * @param tableName 表名称
     */
    public void clearTableFromTable(String tableName) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 取值 自定义文件名称  中的value
     *
     * @param key
     * @param tableName 表名称
     * @return value
     */
    public String getValueByKeyFromTable(String key, String tableName) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    /**
     * @param key
     * @param value
     * @param tableName
     */
    public void saveListString(String key, List<String> value, String tableName) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putStringSet(key, new HashSet(Arrays.asList(value)));
        editor.commit();
    }

    public List<String> getListByKeyFromTable(String key, String tableName) {
        SharedPreferences preferences = MyApplication.instance.getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet(key, new HashSet<String>());
        return new ArrayList<>(new HashSet(stringSet));
    }
}
