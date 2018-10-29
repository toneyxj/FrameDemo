package com.xj.mainframe.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by xj on 2018/10/29.
 */

public class StringUtils {

    /**
     * 空返回true, 否则false;
     *
     * @param checkStr
     * @return
     */
    public static boolean isNull(String checkStr) {
        if (checkStr == null || checkStr.trim().equals("null")
                || checkStr.length() == 0 || checkStr.trim().equals("")
                || checkStr.equals("[]")) {
            return true;
        }
        return false;
    }

    public static String StringToMd5(String psw) {
        {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(psw.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (NoSuchAlgorithmException e) {
                return "";
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
    }
    /**
     * 获得get请求方法URL拼接
     * @param pairs 数据集
     * @param url 请求路径
     * @return 返回数据拼接后的结果
     */
    public static String getGetUrl(Map<String,String> pairs, String url){
        StringBuffer buffer=new StringBuffer();
        buffer.append(url);
        if (pairs!=null&&pairs.size()!=0){
            buffer.append("?");
            //添加get数据
            int i=0;
            for (Map.Entry<String, String> pair : pairs.entrySet()) {
                if (pair.getKey()==null){
                    continue;
                }
                if (pair.getValue()==null){
                    continue;
                }
                if (i!=0){
                    buffer.append("&");
                }
                buffer.append(pair.getKey());
                buffer.append("=");
                buffer.append(pair.getValue());
                i++;
            }
        }
        return buffer.toString();
    }
    /**
     * 将SD卡文件删除
     *
     * @param file 删除路径
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }
    /**
     * 将SD卡文件删除
     *
     * @param path 删除路径
     */
    public static void deleteFile(String path) {
        if (StringUtils.isNull(path))return;
        File file=new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }
}