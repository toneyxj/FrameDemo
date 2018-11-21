package com.xj.mainframe.download.utils;

import com.xj.mainframe.download.DownloadModel;
import com.xj.mainframe.download.listener.DownloadListener;

/**
 * 下载基本方法
 * Created by xj on 2018/11/16.
 */
public abstract class DownloadB {
    private DownloadListener listener;
    private DownloadModel model;

    public DownloadB(DownloadListener listener,DownloadModel model) {
        this.listener = listener;
        this.model=model;
    }

    public DownloadListener getListener() {
        return listener;
    }

    public DownloadModel getModel() {
        return model;
    }

    /**
     * 移除监听
     */
    public void removeListener() {
        this.listener = null;
    }

    /**
     * 开启下载
     */
    public abstract void start();

    /**
     * 暂停下载
     */
    public abstract void pasue();

    /**
     * 删除下载数据及内容
     */
    public abstract void delete();

}
