package com.xj.mainframe.encryption;

import android.util.Base64;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 获得rsa非对称加密秘钥
 * Created by Administrator on 2016/12/19.
 */
public class RSAGet {
    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("PublicKey", publicKey);
        keyMap.put("PrivateKey", privateKey);

        return keyMap;
    }
    /**
     * 取得公钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get("PublicKey");
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }

    /**
     * 取得私钥，并转化为String类型
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get("PrivateKey");
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }
}
