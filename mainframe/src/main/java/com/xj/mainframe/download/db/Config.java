package com.xj.mainframe.download.db;

/**
 * 下载文件配置文件
 * Created by xj on 2018/11/9.
 */

public class Config {
    public final static String DATABASE_NAME="download.db"; //数据库名称
    public final static int DATABASE_VERSION=2; //数据库默认版本
    public final static String TABLE_NAME="down"; //数据库默认版本
    public final static String E_ID="_id";//下载文件路口
    public final static String DOWNLOAD_PATH="path";//下载文件路口
    public final static String DOWNLOAD_SAVE_PATH="savePath";//保存文件路径
    public final static String DOWNLOAD_FILE_SIZE="fileSize";//文件总大小
    public final static String DOWNLOAD_CURRENT_SIZE="currentSize";//当前下载文件大小
    public final static String DOWNLOAD_STATUS="status";//当前下载状态
    public final static String DOWNLOAD_EXTENT="extension";//文件下载扩展字段一般保存一个json字符串
    public final static String DOWNLOAD_Time="downtime";//文件下载扩展字段一般保存一个json字符串

    /** 下载文件状态:-1等待，0成功；1失败；2暂停,100下载中*/
    /** 等待下载 */
    public static final int download_wait=0;
    /** 下载中 */
    public static final int download_loding=1;
    /** 下载成功 */
    public static final int download_success=2;
    /** 下载失败 */
    public static final int download_faile=3;
    /** 暂停下载 */
    public static final int download_pause=4;

    private static String AllLine="";
    public static String allLine(){
        if (AllLine==null||AllLine.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(E_ID);
            builder.append(",");
            builder.append(DOWNLOAD_PATH);
            builder.append(",");
            builder.append(DOWNLOAD_SAVE_PATH);
            builder.append(",");
            builder.append(DOWNLOAD_FILE_SIZE);
            builder.append(",");
            builder.append(DOWNLOAD_CURRENT_SIZE);
            builder.append(",");
            builder.append(DOWNLOAD_STATUS);
            builder.append(",");
            builder.append(DOWNLOAD_EXTENT);
            builder.append(",");
            builder.append(DOWNLOAD_Time);
            AllLine=builder.toString();
        }
        return AllLine;
    }

}
