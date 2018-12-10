package com.xj.mainframe.configer;

import android.Manifest;

/**
 * 权限管理
 * Created by Administrator on 2017/1/9.
 */
public class LimitManager {
    /**
     * 开始获取权限
     */
    public static  final String[] START_LIMIT=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    /**
     * 拍照权限
     */
    public static  final String[] CAMERA=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 读取外部存储卡
     */
    public static  final String[] READ_EXTERNAL_STORAGE=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    /**
     * 外部sd卡写文件
     */
    public static  final String[] WRITE_EXTERNAL_STORAGE=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 读写外部存储卡
     */
    public static  final String[] READ_AND_WRITE_EXTERNAL_STORAGE=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 拨打电话
     */
    public static  final String[] CALL_PHONE=new String[]{Manifest.permission.CALL_PHONE};
    /**
     * 定位权限
     */
    public static  final String[] LOCATION=new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    /**
     * 允许程序录制声音通过手机或耳机的麦克
     */
    public static  final String[] MICROPHONE=new String[]{Manifest.permission.RECORD_AUDIO};
}
