package com.xj.mainframe.utils;

import android.util.Base64;

/**
 * Created by xj on 2018/11/9.
 */

public class Base64Util {

    private static final String UTF_8 = "UTF-8";

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        if (null == inputData) {
            return null;
        }
        try {
            return new String(Base64.decode(inputData.getBytes(UTF_8),Base64.DEFAULT), UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputData;
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        if (null == inputData) {
            return null;
        }
        try {
            return new String(Base64.encode(inputData.getBytes(UTF_8),Base64.DEFAULT), UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputData;
    }
}
