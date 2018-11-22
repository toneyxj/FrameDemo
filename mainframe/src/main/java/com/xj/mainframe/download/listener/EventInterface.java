package com.xj.mainframe.download.listener;

/**
 * 下载的通知事件
 * Created by xj on 2018/11/16.
 */

public interface EventInterface {
    /**
     * 开始下载
     * @param downloadPath
     */
    void onDownloadStart(String downloadPath);
    /**
     * 更新下载进度
     * @param downloadPath 下载文件路径
     * @param curDownlaod 当前下载大小
     * @param count 文件总带下
     * @param currentsize 当前已下载文件大小
     */
    void updateDownload(String downloadPath,int curDownlaod,long count ,long currentsize);
    /**
     * 下载成功
     * @param downloadPath 下载路径
     */
    void onSuccess(String downloadPath);

    /**
     * 下载失败
     * @param downloadPath 下载路径
     * @param msg 下载错误提示
     */
    void onFailed(String downloadPath,String msg);

    /**
     * 暂停下载
     * @param downloadPath 下载路径
     */
    void onPasue(String downloadPath);

    /**
     * 接收下载通知
     * @return true代表接收，false代表不接收
     */
    boolean isAcceptDownload();

}
