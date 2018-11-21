package com.xj.mainframe.download;

import com.xj.mainframe.download.db.Config;
import com.xj.mainframe.download.db.Utils;
import com.xj.mainframe.utils.Base64Util;

/**
 * 数据下在实体类
 * Created by xj on 018/11/9.
 */
public class DownloadModel {
    private  int id;//下载文件路口
    private String path;//下载文件路径
    private String savePath;//保存文件路径
    private long fileSize=0;//文件总大小
    private long currentSize=0;//当前下载文件大小
    private int status= Config.download_wait;//当前下载状态
    private String extension;//文件下载扩展字段一般保存一个json字符串
    private long downtime=System.currentTimeMillis();//加入下载的时间

    //保存文件保存路径时需要转为Base64
    private String B6savePath;
    //保存文件下载路径时需要转为Base64
    private String B6path;

    public int getId() {
        return id;
    }

    public DownloadModel setId(int id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        if (isNull(path)){
            path= Base64Util.decodeData(B6path);
        }
        return path;
    }

    public DownloadModel setPath(String path) {
        this.path = path;
        return this;
    }

    public String getSavePath() {
        if (isNull(B6savePath)&&isNull(savePath)){
            //路径转保存地址
            savePath= Utils.getSavePath(getPath());
        }if (isNull(savePath)){
            savePath= Base64Util.decodeData(B6savePath);
        }else {

        }
        return savePath;
    }

    public DownloadModel setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public DownloadModel setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public DownloadModel setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public DownloadModel setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public DownloadModel setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getB6savePath() {
        if (isNull(B6savePath)){
            B6savePath= Base64Util.encodeData(getSavePath());
        }
        return B6savePath;
    }

    public DownloadModel setB6savePath(String b6savePath) {
        B6savePath = b6savePath;
        return this;
    }

    public String getB6path() {
        if (isNull(B6path)){
            B6path= Base64Util.encodeData(getPath());
        }
        return B6path;
    }

    public DownloadModel setB6path(String b6path) {
        B6path = b6path;
        return this;
    }

    public long getDowntime() {
        return downtime;
    }

    public DownloadModel setDowntime(long downtime) {
        this.downtime = downtime;
        return this;
    }

    private boolean isNull(String path){
        return path==null||path.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "DownloadModel{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", savePath='" + savePath + '\'' +
                ", fileSize=" + fileSize +
                ", currentSize=" + currentSize +
                ", status=" + status +
                ", extension='" + extension + '\'' +
                ", downtime=" + downtime +
                ", B6savePath='" + B6savePath + '\'' +
                ", B6path='" + B6path + '\'' +
                '}';
    }
}
