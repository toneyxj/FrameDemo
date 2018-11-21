package com.xj.mainframe.download.listener;

/**
 * Created by xj on 2018/11/16.
 */

public interface DownloadListener {
    /**
     * 下载成功
     * @param downloadPath 下载路径
     */
    void onSuccess(String downloadPath);

    /**
     * 开始下载
     * @param downloadPath
     */
    void onDownloadStart(String downloadPath);

    /**
     * @param curDownlaod *当前一秒下载量
     */
    void onDownloading(String downloadPath,int curDownlaod,long count ,long currentsize);

    /**
     * 下载失败
     * @param downloadPath 下载路径
     */
    void onFailed(String downloadPath);

    /**
     * 暂停下载
     * @param downloadPath 下载路径
     */
    void onPasue(String downloadPath);
    /**
     * 删除下载
     * @param downloadPath 下载路径
     */
    void onDelete(String downloadPath);


}
