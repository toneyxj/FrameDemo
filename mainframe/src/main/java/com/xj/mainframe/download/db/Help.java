package com.xj.mainframe.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xj.mainframe.configer.APPLog;

/**
 * Created by xj on 2018/11/9.
 */

public class Help extends SQLiteOpenHelper {

    // 初始化类实列
    private static Help instatnce = null;

    public Help(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static Help getInstance(Context context) {
        if (instatnce == null) {
            synchronized (Help.class) {
                if (instatnce == null) {
                    instatnce = new Help(context.getApplicationContext());
                }
            }
        }
        return instatnce;
    }
    /**操作数据时，（增删改查）
     *
     * @param context  上下文对象
     */
    private Help(Context context) {
        this(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        APPLog.e("create database");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        APPLog.e("当第一次运行或者修改数据库时，将会自动执行");

        String createTableSQL = "create table " + Config.TABLE_NAME + "("
                +Config.E_ID        + " integer  primary key autoincrement,"
                +Config.DOWNLOAD_PATH     + " text,"
                +Config.DOWNLOAD_SAVE_PATH + " text,"
                +Config.DOWNLOAD_FILE_SIZE + " INTEGER,"
                +Config.DOWNLOAD_CURRENT_SIZE        + " INTEGER,"
                +Config.DOWNLOAD_STATUS         + " INTEGER,"
                +Config.DOWNLOAD_Time         + " INTEGER,"
                +Config.DOWNLOAD_EXTENT     + " text"
                +")";
        db.execSQL(createTableSQL); // 执行SQL 需要使用execSQL（）
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS " + Config.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        super.onOpen(db);
        System.out.println("当数据库打开时，执行");
    }
}
