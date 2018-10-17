package com.xj.mainframe.encryption;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密算法
 * Created by Administrator on 2016/12/19.
 */
public class AESUtils {
    /**
     * 加密数据
     * @param key
     * @param text
     * @return
     * @throws Exception
     */
    public static String encrypt(String key,String text) throws Exception {
        // 初始化向量参数，AES 为16bytes. DES 为8bytes
        String iv ="bbbbbbbbbbbbbbbb";

        // 两个参数，第一个为私钥字节数组， 第二个为加密方式AES或者DES
        Key keySpec = new SecretKeySpec(key.getBytes(), "AES");

        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
        // 实例化加密类，参数为加密方式，要写全
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 初始化，此方法可以采用三种方式，按服务器要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom
        //（3）采用此代码中的IVParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] bytes = cipher.doFinal(text.getBytes());// 加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,
        // 7bit等等。此处看服务器需要什么编码方式
        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
        return result;

    }

    /**
     * 解密数据
     * @param key
     * @param text
     * @return
     * @throws Exception
     */
    public static String decrypt(String key,String text) throws Exception {

        String iv = "bbbbbbbbbbbbbbbb";
        byte[] textBytes = Base64.decode(text.getBytes(), Base64.DEFAULT);

        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());

        Key keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); // 与加密时不同MODE:Cipher.DECRYPT_MODE

        byte[] bytes = cipher.doFinal(textBytes);
        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
        return result;

    }
}
