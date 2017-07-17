package com.myapp.study.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * 用于处理退出程序时可以退出所有的activity，而编写的通用类
 */
public class ActivityManager {

//	private List<Activity> activityList = new LinkedList<Activity>();
//	private List<Activity> loginList = new LinkedList<Activity>();

    private static ActivityManager instance;
    private LinkedList<WeakReference<Activity>> loginList = new LinkedList<>();
    private LinkedList<WeakReference<Activity>> activityList = new LinkedList<>();
    private LinkedList<Activity> chargeList = new LinkedList<>();

    private ActivityManager() {
    }

    /**
     * 单例模式中获取唯一的AbActivityManager实例.
     *
     * @return
     */
    public static ActivityManager getInstance() {
        if (null == instance) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到容器中.
     *
     * @param activity
     */
    public void addActivity(WeakReference<Activity> activity) {
        activityList.add(activity);
    }

    public void loginAddActivity(Activity activity) {
//		loginList.add(activity);
    }

    public void loginAddActivity(WeakReference<Activity> activity) {
        loginList.add(activity);
    }

    /**
     * 移除Activity从容器中.
     *
     * @param activity
     */
    public void removeActivity(WeakReference<Activity> activity) {
        if (null != activity && null != activity.get()) {
            activityList.remove(activity);
        }

    }

    public void chargeAddActivity(Activity activity) {
        chargeList.add(activity);
    }

    public void clearCharge() {
        for (Activity activity : chargeList) {
            if (null != activity) {
                activity.finish();
            }

        }
    }

    /**
     * 遍历所有Activity并finish.
     */
    public void clearAllActivity() {
        for (WeakReference<Activity> activity : activityList) {
            if (activity != null && null != activity.get()) {
                activity.get().finish();
            }
        }
    }

    public void clearLoginAllActivity() {
        for (WeakReference<Activity> activity : loginList) {
            if (activity != null && null != activity.get()) {
                activity.get().finish();
            }
        }
    }
}