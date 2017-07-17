package com.myapp.study.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.myapp.study.application.MyApplication;
import com.myapp.study.log.LogUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lvgy on 16/3/19.
 */
public class CommonUtil {
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;

    public static String takePhoto(Context context, int requestCode) {
        String filePath = "";
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    TAKE_PHOTO_REQUEST_CODE);
        } else {
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE, null);
            filePath = MyApplication.instance.getCachePath() + File.separator + MD5.md5(String.valueOf(System.currentTimeMillis())) + "camera" + ".png";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(filePath)));
            ((Activity) context).startActivityForResult(intent, requestCode);

        }
        return filePath;
    }

    public static void albumPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 指定格式返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(new Date());
    }

    public static File getSaveFile(String folderPath, String fileName) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static int getActivityCount() {
        int numActivities = 0;
        ActivityManager am = (ActivityManager) MyApplication.instance.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (null != tasks && !tasks.isEmpty()) {
            if (null != tasks.get(0)) {
                LogUtil.e("get activity:" + tasks.get(0).numActivities);
                numActivities = tasks.get(0).numActivities;
            }
        }
        return numActivities;
    }

    public static float getDensity() {
        return MyApplication.instance.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth() {
        DisplayMetrics dm = MyApplication.instance.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static void finish(Activity activity) {
        activity.finish();
    }
}
