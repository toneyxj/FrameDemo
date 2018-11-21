package com.xj.mainframe.download.db;

import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.download.listener.SucceedListener;
import com.xj.mainframe.utils.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.xj.mainframe.utils.StringUtils.isNull;

/**
 * Created by xj on 2018/11/9.
 */

public class Utils {
    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString().toLowerCase();
    }
    /**
     * 将SD卡文件删除
     *
     * @param path 删除路径
     */
    public static void deleteFile(String path) {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }

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
     * 获得下载保存的地址路径
     * @param url 下载地址
     * @return
     */
    public static String getSavePath(String url){
       String path= getSavePath();
        if (path.equals(""))return "";
        File pm=new File(path);
        int index=url.lastIndexOf(".");
        String px="";
        if (index>=0){
            px= url.substring(index);
        }
        if (!pm.exists()){
            pm.mkdirs();
        }else {
            path+="/"+stringToMD5(url)+px;
        }
        APPLog.e("getSavePath",path);
        return path;
    }
    public static String getSavePath(){
        String path= StringUtils.getSDPath();
        if (isNull(path))return "";
        path+="/download";
        return path;
    }

    /**
     * 删除所有文件
     * @param listener
     */
    public static void deleteAllDownload(SucceedListener listener){
        String path= getSavePath();
        if (path.equals(""))return;
        StringUtils.deleteFile(path,listener);
    }
}
