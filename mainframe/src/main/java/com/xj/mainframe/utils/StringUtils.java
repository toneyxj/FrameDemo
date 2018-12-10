package com.xj.mainframe.utils;

import android.os.Environment;

import com.xj.mainframe.download.listener.SucceedListener;

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
     * 图片文件格式
     */
    public static final String IMAGE_FORMAT = "image/*";
    /**
     * 相册，回调值
     */
    public static final int PHOTO_ALBUM = 10000;
    /**
     * 拍照，回调值
     */
    public static final int PHOTOH_TAKE = 10001;
    /**
     * 相片处理结果，回调值
     */
    public static final int PHOTOHT_DISPOSE = 10003;
    /**
     * 获取sd卡路径
     *
     * @return 返回路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        } else {
            return "";
        }
        String path = sdDir.toString() + "/baseData/";
        if (!(new File(path).exists())) {
            new File(path).mkdirs();
        }
        return path;
    }
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
        deleteFile(file,null);
    }
    public static void deleteFile(final File file, final SucceedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteSys(file);
                if (listener!=null)listener.onSucess();
            }
        }).start();
    }
    private static void deleteSys(File file){
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory()) {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                        deleteSys(files[i]); // 把每个文件 用这个方法进行迭代
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
     * @param path 删除路径
     */
    public static void deleteFile(String path) {
        deleteFile(path,null);
    }
    public static void deleteFile(String path, SucceedListener listener) {
        if (StringUtils.isNull(path))return;
        deleteFile(new File(path),listener);
    }

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
}
