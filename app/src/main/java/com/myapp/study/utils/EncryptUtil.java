package com.myapp.study.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lvgy on 16/12/5.
 */

public class EncryptUtil {
    private static String keyEdit(String text, String encrypt_key) {
        char[] encryptArray = MD5.md5(encrypt_key).toCharArray();
        char[] textArray = text.toCharArray();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (char character : textArray) {
            if (count == encryptArray.length) {
                count = 0;
            }
            int value = character ^ encryptArray[count];
            stringBuilder.append((char) value);
            count++;
        }
        return stringBuilder.toString();
    }

    private static String encryptText(String text, String key) {
        char[] encryptArray = MD5.md5(String.valueOf(1)).toCharArray();
        char[] textCharArray = text.toCharArray();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (char character : textCharArray) {
            if (count == encryptArray.length) {
                count = 0;
            }
            int value = character ^ encryptArray[count];
            stringBuilder.append(encryptArray[count]).append((char) value);
            count++;
        }
        return keyEdit(stringBuilder.toString(), key);
    }

    public static String encrypt_url(String url, String key) {
        try {
            return URLEncoder.encode(Base64.encodeToString((encryptText(url, key)).getBytes(), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
