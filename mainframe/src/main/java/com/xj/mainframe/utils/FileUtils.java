package com.xj.mainframe.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import com.xj.mainframe.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * data/data下文件的读取和删除操作
 * Created by Administrator on 2016/5/6.
 */
public class FileUtils {
    public Context context;

    // 初始化类实列
    private static FileUtils instatnce = null;

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static FileUtils getInstance() {
        if (instatnce == null) {
            synchronized (FileUtils.class) {
                if (instatnce == null) {
                    instatnce = new FileUtils();
                }
            }
        }
        return instatnce;
    }

    /**
     * 初始化设置
     *
     */
    public  FileUtils() {
        this.context= BaseApplication.context;
        getfileMksPath();
        getTxtMksPath();
    }

    //写数据
    public void writeFile(String fileName, String writestr) throws IOException {
        createFiles(fileName);
        try {
            File file = new File(fileName);
            FileOutputStream fout = new FileOutputStream(file);

            byte[] bytes = writestr.getBytes();

            fout.write(bytes);

            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @param fileName 文件名
     * @return 返回读取的数据
     * @throws IOException
     */
    public String readFile(String fileName) throws IOException {
        createFiles(fileName);
        String path = fileName;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;

    }

    /**
     * 创建文件
     *
     * @param files
     */
    public void createFiles(String files) throws IOException {
        File data = new File(files);
        if (!data.exists()) {
            data.createNewFile();
        }
    }

    /**
     * 创建目录
     *
     * @param mks
     */
    public void createMks(String mks) {
        File data = new File(mks);
        if (!data.exists()) {
            data.mkdirs();
        }

    }

    /**
     * 获得根目录
     *
     * @return
     */
    public String getDataFilePath() {
        return context.getFilesDir().toString() + "/";
    }

    /**
     * 获得文本文件保存地址目录
     *
     * @return
     */
    public String getTxtMksPath() {
        String maks = getDataFilePath() + "txt/";
        createMks(maks);
        return maks;
    }


    /**
     * 获得db文件保存地址目录
     *
     * @return
     */
    public String getDBMksPath() {
        String maks = getDataFilePath() + "DB/";
        createMks(maks);
        return maks;
    }

    /**
     * 获得文件保存地址目录
     *
     * @return
     */
    public String getfileMksPath() {
        String file = getDataFilePath() + "files/";
        createMks(file);
        return file;
    }

    /**
     * 获得文件保存地址目录
     *
     * @return
     */
    public String getCacheMksPath() {
        String file = getDataFilePath() + "cache/";
        createMks(file);
        return file;
    }

    /**
     * 获得assest文件路径名
     *
     * @param fileName 文件名
     * @return 返回文件路径
     */
    public String getAssestPath(String fileName) {
        return "file:///android_asset/" + fileName;
    }

    /**
     * 获得外部总空间大小
     * @return
     */
    public synchronized String getTotalMenory(){
        // 总空间
        String totalMemory =  Formatter.formatFileSize(context,getTotalMenoryLong());
        return totalMemory;
    }

    public synchronized long getTotalMenoryLong(){
        File path = Environment.getExternalStorageDirectory();
        if (!path.exists())return 0;
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());

        long blockSize =0;  // 获得一个扇区的大小
        long totalBlocks=0 ; // 获得扇区的总数

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize=stat.getBlockSizeLong();
            totalBlocks = stat.getBlockCountLong();
        }else {
            blockSize = stat.getBlockSize();
            totalBlocks = stat.getBlockCount();
        }
        return totalBlocks * blockSize;
    }
    /**
     * 获得外部可用空间大小
     * @return
     */
    public synchronized String getUseMenory(){
        // 总空间
        String totalMemory =  Formatter.formatFileSize(context,getUseMenoryLong());
        return totalMemory;
    }
    public synchronized long getUseMenoryLong(){
        File path = Environment.getExternalStorageDirectory();
        if (!path.exists())return 0;
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());

        long blockSize =0;  // 获得一个扇区的大小
        long availableBlocks =0; // 获得可用的扇区数量
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize=stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        }else {
            blockSize = stat.getBlockSize();
            availableBlocks=stat.getAvailableBlocks();
        }
        // 可用空间
        return  availableBlocks * blockSize;
    }
    /**
     * 获得内部可用空间大小
     * @return
     */
    public String getRootDirectoryMenory(){
        // 总空间
        String totalMemory =  Formatter.formatFileSize(context,getRootDirectoryMenoryLong());
        return totalMemory;
    }
    /**
     * 获得内部存储可用大小
     * @return 可用大小
     */
    public long getRootDirectoryMenoryLong(){
        File path = Environment.getRootDirectory();
        if (!path.exists())return 0;
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());

        long blockSize =0;  // 获得一个扇区的大小
        long availableBlocks =0; // 获得可用的扇区数量
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            stat.getAvailableBlocksLong();
            availableBlocks = stat.getAvailableBlocksLong();
        }else {
            blockSize = stat.getBlockSize();
            availableBlocks=stat.getAvailableBlocks();
        }
        // 总空间
        return  availableBlocks * blockSize;
    }
}
