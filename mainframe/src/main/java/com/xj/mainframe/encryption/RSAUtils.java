package com.xj.mainframe.encryption;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 非对称加密解析工具类
 * Created by Administrator on 2016/12/19.
 */
public class RSAUtils {
    private static  final String KEY_ALGORTHM="RSA";
    /**
     * 用私钥加密
     *
     * @param data
     *            加密数据
     * @param key
     *            密钥
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key)
            throws Exception {
        // 解密密钥
        byte[] keyBytes = Base64.decode(key.getBytes(), Base64.DEFAULT);
        // 取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] resultBytes=cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.encodeToString(resultBytes, Base64.DEFAULT);
    }
    /**
     * 用私钥解密
     *
     * @param data
     *            加密数据
     *
     * @param key
     *            密钥
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key)
            throws Exception {
        // 对私钥解密
        byte[] keyBytes = Base64.decode(key.getBytes(), Base64.DEFAULT);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes=Base64.decode(data.getBytes(), Base64.DEFAULT);
        byte[] resultBytes=cipher.doFinal(dataBytes);
        return new String(resultBytes,"UTF-8");
    }
    /**
     * 用公钥加密
     * @param data   加密数据
     * @param key    密钥
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data,String key)throws Exception{
        //对公钥解密
        byte[] keyBytes =  Base64.decode(key.getBytes(), Base64.DEFAULT);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] resultBytes=cipher.doFinal(data.getBytes());
        return Base64.encodeToString(resultBytes, Base64.DEFAULT);
    }
    /**
     * 用公钥解密
     * @param data   加密数据
     * @param key    密钥
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes =  Base64.decode(key.getBytes(),Base64.DEFAULT);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] dataBytes=Base64.decode(data.getBytes(), Base64.DEFAULT);
        byte[] resultBytes=cipher.doFinal(dataBytes);
        return new String(resultBytes,"UTF-8");
    }
}
