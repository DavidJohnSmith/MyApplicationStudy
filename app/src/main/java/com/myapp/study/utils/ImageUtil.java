package com.myapp.study.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.myapp.study.log.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lvgy on 16/8/24.
 */
public class ImageUtil {
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0) {
                options = 5;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    public static void saveTempFile(String srcPath, String savePath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        LogUtil.e("real weight:" + w + "real height:" + h);
        float hh = 480f;//这里设置高度为480f
        float ww = 320f;//这里设置宽度为320f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        LogUtil.e("path:" + srcPath + "be:" + be);
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        saveBitmapFile(bitmap, savePath);
    }

    public static void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            LogUtil.e("get pic size:" + bitmap.getByteCount() / 1024);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            LogUtil.e("save file error" + e.getMessage());
        } finally {
            LogUtil.e("save file success!");
            if (bitmap != null) {
                bitmap.recycle();
            }

            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException ignore) {
            }

        }
    }
}
