package com.xj.mainframe.download.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.download.DownloadModel;
import com.xj.mainframe.utils.Base64Util;

import java.util.ArrayList;
import java.util.List;

import static com.xj.mainframe.download.Dinterface.DMBase.TAG_MD;

/**
 * 文件下载操作实体类
 * Created by xj on 2018/11/9.
 */
public class Operate {
    // 初始化类实列
    private static Operate instatnce = null;
    /**
     * 数据库初始化
     */
    private static SQLiteDatabase db;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static Operate getInstance(Context context) {
        if (instatnce == null) {
            synchronized (Help.class) {
                if (instatnce == null || db == null) {
                    instatnce = new Operate(context.getApplicationContext());
                }
            }
        }
        return instatnce;
    }


    public Operate(Context context) {
        db = Help.getInstance(context).getWritableDatabase();
    }


    /**
     * 保存文件集合
     *
     * @param model 处理的数据模型
     * @return 是否处理成功
     */
    public synchronized boolean saveMode(DownloadModel model) {
        long id = get_ID(model.getB6path());
        long isSucess;
        hitnLog("in saveMode ");
        if (id == -1) {
            //不存在插入数据
            isSucess = db.insert(Config.TABLE_NAME, Config.allLine(), getContentValues(model));
            hitnLog("in saveMode is " + isSucess + "  and  insert");
        } else {
            //存在就更新下载
            isSucess = db.update(Config.TABLE_NAME, getContentValues(model), Config.E_ID + "=?", new String[]{String.valueOf(id)});
            hitnLog("in saveMode is " + isSucess + "  and  update");
        }
        return isSucess >= 1;
    }
    //-------------- 数据删除

    public synchronized void delete(boolean isB6,String... paths) {
        db.beginTransaction();
        db.execSQL(getDeleteValueStr(isB6,paths));
        db.endTransaction();
    }

    public synchronized void delete(boolean isB6,int... ids) {
        db.beginTransaction();
        db.execSQL(getDeleteValueInt(isB6,ids));
        db.endTransaction();
    }

    private String getDeleteValueStr(boolean isB6,String... values) {
        if (values.length <= 0) values[0] ="";
        StringBuilder builder = new StringBuilder();
        int size=values.length-1;
        for (int i = 0; i <= size; i++) {
                String vv=values[i].toString();
                if (!isB6){
                    vv=Base64Util.encodeData(vv);
                }
                builder.append("'"+vv+"'");
                if (i<size){
                    builder.append(",");
                }

        }

        String sql = "delete " + Config.TABLE_NAME + " where in (" +builder.toString() +")";
        hitnLog("getDeleteValue:"+sql);
        return sql;
    }
    private String getDeleteValueInt(boolean isB6,int... values) {
        if (values.length <= 0) values[0] = -10;
        StringBuilder builder = new StringBuilder();
        int size=values.length-1;
        for (int i = 0; i <= size; i++) {
                builder.append(values[i]);
                if (i<size){
                    builder.append(",");
                }
        }

        String sql = "delete " + Config.TABLE_NAME + " where in (" +builder.toString() +")";
        hitnLog("getDeleteValue:"+sql);
        return sql;
    }

    /**
     * 清空表数据
     *
     * @return
     */
    public synchronized void clearTable() {
        String sql = "truncate table " + Config.TABLE_NAME;
        db.execSQL(sql);
        hitnLog("in clearTable sql is: " + sql);
    }

    //-------------- 数据查找

    /**
     * 获得所有的下载数据信息，按加入时间 排序
     * @return
     */
    public synchronized List<DownloadModel> getAll(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得已经下载完成的数据列表
     * @return
     */
    public synchronized List<DownloadModel> getDownloaded(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"="+Config.download_success+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得正在下载中的文件信息
     * @return
     */
    public synchronized  List<DownloadModel> getDonwnloading(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"="+Config.download_loding+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得等待下载的数据列表
     * @return
     */
    public synchronized List<DownloadModel> getDownloadWait(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"="+Config.download_success+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得下载失败的文件集合
     * @return
     */
    public synchronized List<DownloadModel> getDownlaodFaileds(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"="+Config.download_faile+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得需要下载的数据集合
     * @return
     */
    public synchronized List<DownloadModel> getNeedDownloads(){
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"<>"+Config.download_success+" order by "+Config.DOWNLOAD_Time+" ASC";//先下载的排在最前面
        return getDownlaodDate(sql);
    }

    /**
     * 获得下载数据
     * @return
     */
    private synchronized List<DownloadModel> getDownlaodDate(String sql){
        List<DownloadModel> list=new ArrayList<>();
        Cursor cur=db.rawQuery(sql,null);
        if (cur!=null){
            while (cur.moveToNext()){
                list.add(getModel(cur));
            }
            cur.close();
        }
        return list;
    }
    public synchronized  DownloadModel getByUrlLoadMode(String url){
        DownloadModel model=new DownloadModel().setPath(url);
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_PATH+"='"+model.getB6path()+"'";//先下载的排在最前面
        Cursor cur=db.rawQuery(sql,null);
        if (cur!=null){
            if (cur.moveToNext()){
                model=getModel(cur);
            }
            cur.close();
        }
        return model;

    }
    /**
     * 获得下一个下载的数据,下载失败的数据除外
     * @return
     */
    public synchronized List<DownloadModel> getDownloadLimitModels(int downSize,String... hasDownloads){
        StringBuilder builder=new StringBuilder();
        if (hasDownloads!=null) {
            for (int i = 0; i < hasDownloads.length; i++) {
                builder.append(" and ");
                builder.append(Config.DOWNLOAD_PATH + "<>'" + hasDownloads[0]+"'");
            }
        }
        String hasd=builder.toString();
        List<DownloadModel> models=new ArrayList<>();
        String sql="select "+Config.allLine()+" from "+Config.TABLE_NAME+" where "+Config.DOWNLOAD_STATUS+"="+Config.download_loding+hasd+" order by "+Config.DOWNLOAD_Time+" ASC limit 0," + downSize;//先下载的排在最前面
        APPLog.e(TAG_MD,"sql1:"+sql);
        Cursor cur=db.rawQuery(sql,null);
        if (cur!=null){
            while (cur.moveToNext()){
                models.add(getModel(cur));
            }
            cur.close();
        }
        if (models.size()<downSize) {
            downSize-=models.size();
            sql = "select " + Config.allLine() + " from " + Config.TABLE_NAME + " where " + Config.DOWNLOAD_STATUS + "=" + Config.download_pause + " or " + Config.DOWNLOAD_STATUS + "=" + Config.download_wait +hasd+ " order by " + Config.DOWNLOAD_Time + " ASC limit 0," + downSize;//先下载的排在最前面
            APPLog.e(TAG_MD,"sql2:"+sql);
             cur = db.rawQuery(sql, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    models.add(getModel(cur));
                }
                cur.close();
            }
        }
        return models;
    }


    //-------------- 数据修改
    /**
     * 更新一条数据的所有内容
     *
     * @param model
     * @return
     */
    public synchronized boolean updateAll(DownloadModel model) {
        boolean update = false;
//        delete from 表名 where id in (1,3,5)
        hitnLog("in updateAll is " + update);
        return saveMode(model);
    }
    /**
     * 更新下载进度
     *
     * @param downloadPath
     * @param totalSize
     * @param curentSize
     * @return
     */
    public synchronized boolean updateProgress(String downloadPath, long totalSize, long curentSize) {
        return updateB6Progress(Base64Util.encodeData(downloadPath),totalSize,curentSize);
    }
    public synchronized boolean updateB6Progress(String B6path, long totalSize, long curentSize) {
        //存在就更新下载
        int ss = db.update(Config.TABLE_NAME, getContentProgress(totalSize, curentSize), Config.DOWNLOAD_PATH + "=?", new String[]{B6path});
//        delete from 表名 where id in (1,3,5)
        hitnLog("in updateProgress is " + ss);
        return ss > 0;
    }

    /**
     * 更新下载状态
     *
     * @param downloadPath,下载文件url
     * @param status
     * @return
     */
    public synchronized boolean updateStatus(String downloadPath, int status) {
        return updateB6Status(Base64Util.encodeData(downloadPath),status);
    }
    /**
     * 更新下载状态
     *
     * @param B6path
     * @param status
     * @return
     */
    public synchronized boolean updateB6Status(String B6path, int status) {
        //存在就更新下载
        int ss = db.update(Config.TABLE_NAME, getContentStatus(status), Config.DOWNLOAD_PATH + "=?", new String[]{B6path});
        hitnLog("in updateStatus is " + ss);
        return ss > 0;
    }


    private ContentValues getContentValues(DownloadModel model) {
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

    private ContentValues getContentProgress(long totalSize, long curentSize) {
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_FILE_SIZE, totalSize);
        values.put(Config.DOWNLOAD_CURRENT_SIZE, curentSize);
        return values;
    }

    private ContentValues getContentStatusProgress(long totalSize, long curentSize, int status) {
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_FILE_SIZE, totalSize);
        values.put(Config.DOWNLOAD_CURRENT_SIZE, curentSize);
        values.put(Config.DOWNLOAD_STATUS, status);
        return values;
    }

    private ContentValues getContentStatus(int status) {
        ContentValues values = new ContentValues();
        values.put(Config.DOWNLOAD_STATUS, status);
        return values;
    }

    /**
     * 判断数据是否存在
     *
     * @param path 下载文件路径
     * @return 存在返回true
     */
    public synchronized boolean isExit(String path) {
        boolean is = isExitB6(Base64Util.encodeData(path));
        hitnLog("in isExit is " + is);
        return is;
    }

    /**
     * 判断数据是否存在
     *
     * @param B6path 下载文件base64编码路径
     * @return 存在返回true
     */
    public synchronized boolean isExitB6(String B6path) {
        boolean is = get_ID(B6path) >= 0;
        hitnLog("in isExitB6 is " + is);
        return get_ID(B6path) >= 0;
    }

    /**
     * 获得下载文件的id
     *
     * @param B6path 下载文件的base地址
     * @return
     */
    public synchronized int get_ID(String B6path) {
        String sql = "select " + Config.E_ID + " from " + Config.TABLE_NAME + " where " + Config.DOWNLOAD_PATH + " ='" + B6path + "'";
        Cursor cur = db.rawQuery(sql, null);
        if (cur == null) return -1;
        int id = -1;
        if (cur.moveToNext()) {
            id = cur.getInt(0);
        }
        cur.close();
        hitnLog("in get_ID is " + id);
        return id;
    }

    private void hitnLog(Object value) {
        Log.d("operate Download", value.toString());
    }

    /**
     * cur里面的内容转mddel
     * @param cur
     * @return
     */
    private DownloadModel getModel(Cursor cur){
       return new DownloadModel().setId(cur.getInt(0))
               .setB6path(cur.getString(1))
               .setB6savePath(cur.getString(2))
               .setFileSize(cur.getLong(3))
               .setCurrentSize(cur.getLong(4))
               .setStatus(cur.getInt(5))
               .setExtension(cur.getString(6))
               .setDowntime(cur.getLong(7));
    }
}
