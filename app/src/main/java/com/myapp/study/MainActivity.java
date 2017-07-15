package com.myapp.study;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.myapp.study.log.LogUtil;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.my_image_view)
    SimpleDraweeView myImageView;
    private String url = "http://img04.tooopen.com/images/20131115/sy_47505221718.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LogUtil.e(Base64.encodeToString("a".getBytes(), Base64.DEFAULT));
        LogUtil.e(new String(Base64.decode("YQ==".getBytes(), Base64.DEFAULT)));

        SecureRandom sr = new SecureRandom();
        byte[] output = new byte[16];
        sr.nextBytes(output);

        LogUtil.e(new String(output));

        byte[] input = "abc".getBytes();
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-512");
            sha.update(input);
            byte[] output1 = sha.digest();
            LogUtil.e("get sha-256:" + new String(output1).length());
            String result = Base64.encodeToString(output1, Base64.DEFAULT);
            LogUtil.e("get sha-256:" + result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(1048);
            //产生密钥
            SecretKey secretKey = keyGenerator.generateKey();
//获取密钥
            byte[] key = secretKey.getEncoded();
            LogUtil.e("get key:" + new String(key) + Base64.encodeToString(key, Base64.DEFAULT));

//还原密钥
            SecretKey restoreSecretKey = new SecretKeySpec(key, "HMACSHA512");
//实例化MAC
            Mac mac = Mac.getInstance(restoreSecretKey.getAlgorithm());
//初始化MAC
            mac.init(restoreSecretKey);
//执行摘要
            byte[] hmacSHA256Bytes = mac.doFinal("abc".getBytes());
            String result = Base64.encodeToString(hmacSHA256Bytes, Base64.DEFAULT);
            LogUtil.e("get result:" + result);


            KeyGenerator keyGeneratorAES = KeyGenerator.getInstance("AES");
            keyGeneratorAES.init(256);
//产生密钥
            SecretKey secretKeyAES = keyGeneratorAES.generateKey();
//获取密钥
            byte[] keyBytes = secretKeyAES.getEncoded();
            LogUtil.e("AES KEY" + Base64.encodeToString(keyBytes, 0));

//还原密钥
            SecretKey keyAES = new SecretKeySpec(keyBytes, "AES");

//加密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keyAES);
            byte[] encodeResult = cipher.doFinal("abc".getBytes());
            LogUtil.e("AES encode" + Base64.encodeToString(encodeResult, Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.my_image_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_image_view:
                LogUtil.e("this is click");
                break;
        }
    }

    private Bitmap getBitmapFromCache(String url) {

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchImageFromBitmapCache(imageRequest, CallerThreadExecutor.getInstance());
        try {
            CloseableReference<CloseableImage> imageReference = dataSource.getResult();
            if (imageReference != null) {
                try {
                    CloseableBitmap image = (CloseableBitmap) imageReference.get();
                    // do something with the image
                    Bitmap loadedImage = image.getUnderlyingBitmap();
                    if (loadedImage != null) {
                        return loadedImage;
                    } else {
                        return null;
                    }
                } finally {
                    CloseableReference.closeSafely(imageReference);
                }
            }
        } finally {
            dataSource.close();
        }
        return null;
    }
}
