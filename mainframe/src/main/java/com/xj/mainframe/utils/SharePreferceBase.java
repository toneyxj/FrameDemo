package com.xj.mainframe.utils;

import android.content.Context;

import com.xj.mainframe.download.Dinterface.DMBase;

/**
 * 项目基本的内存数据库保存数据
 * Created by xj on 2018/11/21.
 */
public class SharePreferceBase {
    //设置下载文件时是否是可以在手机网络下下载
    private static final String DOWN_IS_MOBIL="downlaod_is_mobil";

    /**
     * s是否可以在非wifi网络下下载文件
     * @param context 上下文
     * @return true代表可以在非wifi网络下下载文件
     */
    public static boolean getIsMobile(Context context){
        return SharePreferceUtil.getInstance(context).getBoolean(DOWN_IS_MOBIL);
    }
    public static void setIsMobile(Context context,boolean ismobile){
        SharePreferceUtil.getInstance(context).setCache(DOWN_IS_MOBIL,ismobile);
        //设置后重新设置需要设置的地方
        DMBase.getInstance(context).initSetting();
    }

}
