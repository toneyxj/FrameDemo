package com.xj.mainframe.download.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xj.mainframe.download.DownloadModel;
import com.xj.mainframe.utils.Base64Util;

/**
 * 文件下载操作实体类
 * Created by xj on 2018/11/9.
 */
public  class Operate {
    // 初始化类实列
    private static Operate instatnce = null;
    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static Operate getInstance(Context context) {
        if (instatnce == null) {
            synchronized (Help.class) {
                if (instatnce == null) {
                    instatnce = new Operate(context.getApplicationContext());
                }
            }
        }
        return instatnce;
    }

    /**
     * 数据库初始化
     */
    private SQLiteDatabase db;

    public Operate(Context context) {
        db=Help.getInstance(context).getWritableDatabase();
    }


    /**
     * 保存文件集合
     *
     * @param model 处理的数据模型
     * @return 是否处理成功
     */
    public synchronized boolean saveMode(DownloadModel model) {
        long id=get_ID(model.getB6path());
        long isSucess;
        if (id==-1) {
            //存在就更新下载
            isSucess = db.update(Config.TABLE_NAME, getContentValues(model), Config.E_ID + "=?", new String[]{String.valueOf(id)});
        } else {
            //不存在插入数据
            isSucess = db.insert(Config.TABLE_NAME, Config.allLine(), getContentValues(model));
        }
        return isSucess >= 1;
    }
    //-------------- 数据删除

    public synchronized boolean delete(String... paths){
//        delete from 表名 where id in (1,3,5)
        return false;
    }
    public synchronized boolean delete(int... ids){
        return false;
    }

    /**
     * 清空表数据
     * @return
     */
    public synchronized boolean clearTable(){
        return false;
    }

    //-------------- 数据查找

    //-------------- 数据修改
    /**
     * 更新一条数据的所有内容
     * @param model
     * @return
     */
    public synchronized boolean updateAll(DownloadModel model){
        return saveMode(model);
    }

    /**
     * 更新下载进度
     * @param B6path
     * @param totalSize
     * @param curentSize
     * @return
     */
    public synchronized boolean updateProgress(String B6path, long totalSize,long curentSize ){
        //存在就更新下载
        int ss=db.update(Config.TABLE_NAME, getContentProgress(totalSize,curentSize), Config.DOWNLOAD_PATH + "=?", new String[]{B6path});
        return ss>0;
    }

    /**
     * 更新下载状态
     * @param B6path
     * @param status
     * @return
     */
    public synchronized boolean updateStatus(String B6path,int status){
        //存在就更新下载
        int ss=db.update(Config.TABLE_NAME, getContentStatus(status), Config.DOWNLOAD_PATH + "=?", new String[]{B6path});
        return ss>0;
    }


    private  ContentValues getContentValues(DownloadModel model) {
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_PATH, model.getB6path());
        values.put(Config.DOWNLOAD_SAVE_PATH, model.getB6savePath());
        values.put(Config.DOWNLOAD_FILE_SIZE, model.getFileSize());
        values.put(Config.DOWNLOAD_CURRENT_SIZE, model.getCurrentSize());
        values.put(Config.DOWNLOAD_STATUS, model.getStatus());
        values.put(Config.DOWNLOAD_Time, model.getDowntime());
        values.put(Config.DOWNLOAD_EXTENT, model.getExtension());
        return values;
    }

    private  ContentValues getContentProgress(long totalSize,long curentSize ){
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_FILE_SIZE,totalSize);
        values.put(Config.DOWNLOAD_CURRENT_SIZE, curentSize);
        return values;
    }
    private ContentValues getContentStatusProgress(long totalSize,long curentSize,int status ){
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_FILE_SIZE,totalSize);
        values.put(Config.DOWNLOAD_CURRENT_SIZE, curentSize);
        values.put(Config.DOWNLOAD_STATUS,status);
        return values;
    }
    private ContentValues getContentStatus(int status ){
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_STATUS,status);
        return values;
    }

    /**
     * 判断数据是否存在
     * @param path 下载文件路径
     * @return 存在返回true
     */
    public synchronized boolean isExit(String path){
        return isExitB6(Base64Util.encodeData(path));
    }
    /**
     * 判断数据是否存在
     * @param B6path 下载文件base64编码路径
     * @return 存在返回true
     */
    public synchronized boolean isExitB6(String B6path){
        return get_ID(B6path)>=0;
    }

    /**
     * 获得下载文件的id
     * @param B6path 下载文件的base地址
     * @return
     */
    public synchronized int get_ID(String B6path){
        String sql="select "+Config.E_ID+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_PATH+" ='"+B6path+"'";
        Cursor cur = db.rawQuery(sql, null);
        if (cur == null )return -1;
        int id=-1;
        if (cur.moveToNext()){
            id=cur.getInt(0);
        }
        cur.close();
        return id;
    }
}
