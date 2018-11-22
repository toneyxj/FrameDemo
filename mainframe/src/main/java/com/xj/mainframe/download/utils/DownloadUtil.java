package com.xj.mainframe.download.utils;

import com.xj.mainframe.BaseApplication;
import com.xj.mainframe.download.DownloadModel;
import com.xj.mainframe.download.db.Utils;
import com.xj.mainframe.download.listener.DownloadListener;
import com.xj.mainframe.netState.NetWorkUtil;
import com.xj.mainframe.utils.FileUtils;
import com.xj.mainframe.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xj on 2018/5/21.
 */

public class DownloadUtil extends DownloadB {
    private int faileSize = 0;
    private boolean isPasue = false;
    private boolean isDelete = false;
    private long sendTime=0;
    private boolean isStart=false;

    public DownloadUtil(DownloadListener listener, DownloadModel model) {
        super(listener, model);
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
   public void start() {
        if (!isStart) {
            download(getModel());
        }
    }

    @Override
    public void pasue() {
        isPasue = true;
    }

    @Override
    public void delete() {
        isDelete = true;
    }

    //--------------------------具体实现方法

    /**
     * @param model 下载数据集合
     */
    public void download(final DownloadModel model) {
        new Thread() {
            @Override
            public void run() {
                if (getListener() != null)
                    getListener().onDownloadStart(model.getPath());
                isStart=true;
                InputStream is = null;
                RandomAccessFile savedFile = null;
                long downloadLength = 0;   //记录已经下载的文件长度
                //文件下载地址
                String downloadUrl = model.getPath();
                //得到下载内容的大小
                long contentLength = getContentLength(downloadUrl);
                //获取文件大小失败
                if (contentLength == 0) {
                    //误误网络状态下或者下载地址已不存在
                    onFail(model);//重新启动
                    return;
                }
                if (model.getFileSize() > 0 && model.getFileSize() != contentLength) {
                    //文件存在但文件大小已改变删除文件
                    Utils.deleteFile(model.getSavePath());
                }
                //创建一个文件
                File file = new File(model.getSavePath());
                if (file.exists()) {
                    //文件存在，得到文件的大小
                    downloadLength = file.length();
                }
                if (contentLength == downloadLength) {
                    if (getListener() != null) {
                        getListener().onSuccess(model.getPath());
                    }
                    return;
                }

                OkHttpClient client = new OkHttpClient();
                /**
                 * HTTP请求是有一个Header的，里面有个Range属性是定义下载区域的，它接收的值是一个区间范围，
                 * 比如：Range:bytes=0-10000。这样我们就可以按照一定的规则，将一个大文件拆分为若干很小的部分，
                 * 然后分批次的下载，每个小块下载完成之后，再合并到文件中；这样即使下载中断了，重新下载时，
                 * 也可以通过文件的字节长度来判断下载的起始点，然后重启断点续传的过程，直到最后完成下载过程。
                 */
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + downloadLength + "-")  //断点续传要用到的，指示下载的区间
                        .url(downloadUrl)
                        .build();
                Response response = null;
                getListener().onDownloading(model.getPath(),0,contentLength, downloadLength);
                try {
                    response = client.newCall(request).execute();
                    if (response != null) {
                        is = response.body().byteStream();
                        savedFile = new RandomAccessFile(file, "rw");
                        savedFile.seek(downloadLength);//跳过已经下载的字节
                        byte[] b = new byte[1024];
                        int total=0;
                        int len;
                        while ((len = is.read(b)) != -1) {
                            savedFile.write(b, 0, len);
                            //计算已经下载的百分比
                            total+=len;
                            long cutT=System.currentTimeMillis();
                            if (Math.abs(cutT-sendTime)>=1000) {
                                sendTime=cutT;
                                downloadLength += total;
                                if (getListener() != null)
                                    getListener().onDownloading(model.getPath(),total, contentLength, downloadLength);
                                faileSize=0;
                                total=0;
                            }
                            if (isDelete || isPasue) {
                                onFail(model);
                                return;
                            }
                        }
                        if (getListener() != null) getListener().onSuccess(model.getPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onFail(model);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (savedFile != null) {
                            savedFile.close();
                        }
                        if (isDelete && file != null) {
                            file.delete();
                        }
                        if (response!=null&&response.body()!=null){
                            response.body().close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void onFail( DownloadModel model) {
        if (getListener() == null) return;
        if (isDelete) {
            getListener().onDelete(model.getPath());
            isStart=false;
        } else if (isPasue) {
            getListener().onPasue(model.getPath());
            isStart=false;
        } else {
            if (isNotDown()) {
                if (faileSize < 5) {
                    faileSize++;
                    download(model);
                } else {
                    getListener().onFailed(model.getPath());
                    //重复下载均出现失败，删除项目，标记下载项目为错误
                    StringUtils.deleteFile(model.getSavePath());
                    isStart=false;
                }
            }else {
//                getListener().onPasue(model.getPath());
                getListener().onFailed(model.getPath());
                isStart=false;
            }
        }
    }
    public boolean isNotDown(){
        boolean isf= FileUtils.getInstance().getUseMenoryLong()>1024*1024*5;
        //文件剩余大小小于10
        boolean isn=NetWorkUtil.isNetworkConnected(BaseApplication.context);
        return isf&&isn;
    }

    /**
     * 得到下载内容的大小
     *
     * @param downloadUrl 下载地址
     * @return 返回文件下载长度
     */
    private long getContentLength(String downloadUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                return contentLength;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

