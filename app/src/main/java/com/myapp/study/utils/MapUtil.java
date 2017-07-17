package com.myapp.study.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于hasmap 转换成url
 */
public class MapUtil {
    public static String map2UrlParams(String url, Map<String, Object> map) {

        StringBuilder sb = new StringBuilder();
        sb.append(url);

        if (map != null && map.size() > 0) {
//            sb.append('?');
            sb.append('&');//修改为&,前面已经传过参数
            for (Map.Entry<String, ?> entry : map.entrySet()) {

                try {
                    if (entry.getValue() != null) {
                        sb.append(entry.getKey())
                                .append('=')
                                .append(URLEncoder.encode(entry.getValue()
                                        .toString(), "UTF-8")).append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String mapToUrlParams(String url, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (null != map && map.size() > 0) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                if (entry.getKey().startsWith("splash")) {
                    sb.append("/").append(entry.getValue());
                } else {
                    sb.append("?");
                    try {
                        if (entry.getValue() != null) {
                            sb.append(entry.getKey())
                                    .append('=')
                                    .append(URLEncoder.encode(entry.getValue()
                                            .toString(), "UTF-8")).append('&');
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    sb.deleteCharAt(sb.length() - 1);

                    if (sb.toString().contains("?")) {
                        String temp = sb.toString().replace("?", "&");
                        Pattern p = Pattern.compile("&");
                        Matcher m = p.matcher(temp);
                        sb.setLength(0);
                        sb.append(m.replaceFirst("?"));
                    }
                }
            }


        }
//        Logger.e(">>> mapToUrlParams >>>" + sb.toString());
        return sb.toString();
    }

    public static String getVlaue(String key, Map<String, String> map) {
        if (map.get(key) == null) {
            return "";
        }
        return map.get(key);
    }
}
