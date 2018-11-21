package com.xj.mainframe.download.Dinterface;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xj.mainframe.download.DownloadManager;
import com.xj.mainframe.download.listener.EventInterface;
import com.xj.mainframe.download.listener.SucceedListener;

/**
 * 基本下载类
 * Created by xj on 2018/11/20.
 */

public abstract class DMBase {
    public static final String TAG_MD="DMBase";
    // 初始化类实列
    private static DMBase instatnce = null;
    public static DMBase getInstance(Context context) {
        if (instatnce == null) {
            synchronized (DMBase.class) {
                if (instatnce == null) {
                    instatnce = new DownloadManager(context.getApplicationContext());
                }
            }
        }
        return instatnce;
    }

    /**
     * 设置参数改变后重置
     */
    public abstract void initSetting();
    /**
     * 清空下载manager
     */
    public abstract void stopDownloadManager();

    /**
     * 直接下载文件，绕过排队
     * @param url 下载文件地址
     */
    public abstract void directDownload(@NonNull String url);
    /**
     * 添加下载文件地址
     * @param url 下载文件地址
     */
    public abstract void addDownload(@NonNull String url);

    /**
     * 停止所有下载
     */
    public abstract void stopAllDownload();
    /**
     * 开始下载
     */
    public abstract void startDownload();
    /**
     * 切换下载：暂停与开始下载
     *
     * @param url
     */
    public abstract void switchDownload(@NonNull String url);

    /**
     * 删除下载记录，
     * @param listener 监听返回在非主线程
     */
    public abstract void deleteDown(SucceedListener listener,@NonNull String... url);

    /**
     * 删除所有下载记录
     * @param listener 监听返回在非主线程
     */
    public abstract void deleteAllDown(SucceedListener listener);
    /**
     * 注册通知事件
     * @param event 注册事件
     */
    public abstract void registerEvent(@NonNull EventInterface event);
    /**
     * 解注册事件
     * @param event 注册事件
     */
    public abstract void unRegisterEvent(@NonNull EventInterface event);


}
