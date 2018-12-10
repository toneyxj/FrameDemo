package com.xj.mainframe.download;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.dialog.AlertDialog;
import com.xj.mainframe.download.Dinterface.DMBase;
import com.xj.mainframe.download.db.Config;
import com.xj.mainframe.download.db.Operate;
import com.xj.mainframe.download.db.Utils;
import com.xj.mainframe.download.listener.DownloadListener;
import com.xj.mainframe.download.listener.EventInterface;
import com.xj.mainframe.download.listener.SucceedListener;
import com.xj.mainframe.download.utils.DownloadB;
import com.xj.mainframe.download.utils.DownloadUtil;
import com.xj.mainframe.netState.NetChangeObserver;
import com.xj.mainframe.netState.NetWorkStateUtil;
import com.xj.mainframe.netState.NetWorkUtil;
import com.xj.mainframe.utils.FileUtils;
import com.xj.mainframe.utils.SharePreferceBase;
import com.xj.mainframe.utils.SharePreferceUtil;
import com.xj.mainframe.utils.StringUtils;
import com.xj.mainframe.utils.SystemUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文件下载管理器
 * Created by xj on 2018/11/8.
 */
public class DownloadManager extends DMBase implements DownloadListener {
    private static final String DM_JSON = "dm_json";
    /**
     * 同时下载文件的个数
     */
    private int MAX_DOWNLOADS = 2;
    private boolean isStop = false;
    private Handler handler = new Handler();
    // 初始化类实列
    private static DownloadManager instatnce = null;

    private Context context;
    /**
     * 事件通知事件处理
     */
    private Set<EventInterface> events = new HashSet<>();
    /**
     * 下载事件处理,默认
     */
    private List<DownloadB> downloads = new ArrayList<>();
    /**
     * 默认情况下手机网络不可下载
     */
    private boolean ismobilDownload = false;
    private AlertDialog alertDialog = null;

    public DownloadManager(Context context) {
        this.context = context;
        MAX_DOWNLOADS = ((int) (SystemUtils.getCPUCoreNum() * 0.5));
        if (MAX_DOWNLOADS < 1) MAX_DOWNLOADS = 1;
        //初始化设置数据
        ismobilDownload = SharePreferceBase.getIsMobile(context);
        //初始化上次下载的文件
        String urls = SharePreferceUtil.getInstance(context).getString(DownloadManager.DM_JSON);
        if (!StringUtils.isNull(urls)) {
            List<String> lists = JSON.parseArray(urls, String.class);
            for (String v : lists) {
                directDownload(v);
            }
        }
        NetWorkStateUtil.getInstance(context).registerObserver(observer);

    }
   private NetChangeObserver observer=new NetChangeObserver() {
        @Override
        public void onConnect(NetWorkUtil.netType type) {
            //网络有连接
            if (canDownload(false)){
                startDownload();
            }else {
                stopAllDownload();
            }
        }

        @Override
        public void onDisConnect() {
            stopAllDownload();
        }
    };

    @Override
    public void initSetting() {
        ismobilDownload = SharePreferceBase.getIsMobile(context);
        boolean can = canDownload(false);
        for (DownloadB dow : downloads) {
            if (can) {
                dow.start();
            } else {
                dow.pasue();
            }
        }
        if (can) {
            initDownload();
        }
    }

    /**
     * 关闭下载manager
     */
    @Override
    public void stopDownloadManager() {
        handler.removeCallbacksAndMessages(null);
        events.clear();
        List<String> urls = new ArrayList<>();
        for (DownloadB d : getDownloads()) {
            d.pasue();
            urls.add(d.getModel().getPath());
        }
        //保存本次下载书籍信息
        String json = JSON.toJSONString(urls);
        APPLog.e("DownloadManager-clearDownloadManager", json);
        SharePreferceUtil.getInstance(context).setCache(DownloadManager.DM_JSON, json);
        getDownloads().clear();

        NetWorkStateUtil.getInstance(context).removeObserver(observer);
        instatnce = null;
    }

    /**
     * 直接下载文件，绕过排队
     *
     * @param url 下载文件地址
     */
    @Override
    public void directDownload(@NonNull String url) {
        DownloadModel model = Operate.getInstance(context).getByUrlLoadMode(url);
        if (model.getFileSize() == 0) {//未获取到文件大小，表示未保存
            //未保存数据
            Operate.getInstance(context).saveMode(model);
        }
        DownloadB downloadB = new DownloadUtil(this, model);
        getDownloads().add(downloadB);
        if (getDownloads().size() > MAX_DOWNLOADS) {
            DownloadB dlb = getDownloads().remove(0);
            dlb.pasue();
        }
        if (canDownload(true)) {
            downloadB.start();
        }
    }

    /**
     * 添加下载文件地址
     *
     * @param url 下载文件地址
     */
    @Override
    public void addDownload(@NonNull String url) {
        if (!Operate.getInstance(context).isExit(url)) {
            //下载地址保存
            DownloadModel model = new DownloadModel().setPath(url);
            Operate.getInstance(context).saveMode(model);
        }
        if (canDownload(true)) {
            initDownload();
        }
    }

    /**
     * 停止所有下载
     */
    @Override
    public void stopAllDownload() {
        isStop = true;
        for (DownloadB d : getDownloads()) {
            d.pasue();
        }
        getDownloads().clear();
    }

    /**
     * 开始下载
     */
    @Override
    public void startDownload() {
        isStop = false;
        for (DownloadB d : getDownloads()) {
            d.start();
        }
        initDownload();
    }

    /**
     * 切换下载：暂停与开始下载
     *
     * @param url
     */
    @Override
    public void switchDownload(@NonNull String url) {
        DownloadB downB = null;
        for (DownloadB downloadB : getDownloads()) {
            if (downloadB.getModel().getPath().equals(url)) {
                downB = downloadB;
                break;
            }
        }
        if (downB == null) {//未下载的文件，切换到下载状态
            directDownload(url);
        } else {//下载下的文件暂停下载
            getDownloads().remove(downB);
            initDownload();
            downB.pasue();
        }
    }

    @Override
    public void deleteDown(final SucceedListener listener, @NonNull final String... urls) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DownloadB> removeList = new ArrayList<DownloadB>();
                for (String url : urls) {
                    for (DownloadB d : getDownloads()) {
                        String path = d.getModel().getPath();
                        if (path.equals(url)) {
                            d.delete();
                            removeList.add(d);
                        }
                    }
                    //删除文件
                    Utils.deleteFile(Utils.getSavePath(url));
                }
                for (DownloadB downloadB : removeList) {
                    getDownloads().remove(downloadB);
                }
                //删除数据保存
                Operate.getInstance(context).delete(false, urls);
                listener.onSucess();

                if (canDownload(false))
                    initDownload();
            }
        }).start();

    }

    @Override
    public void deleteAllDown(SucceedListener listener) {
        List<String> urls = new ArrayList<>();
        for (DownloadB d : getDownloads()) {
            d.pasue();
            urls.add(d.getModel().getPath());
        }
        getDownloads().clear();
        //删除数据库存储
        Operate.getInstance(context).clearTable();
        //删所有的下载数据
        Utils.deleteAllDownload(listener);
    }

    /**
     * 初始化下载数据,获取下载项目
     */
    private void initDownload() {
        if (isStop && !canDownload(false)) return;
        //加载的数据没有满的情况
        int downS = getDownloads().size();
        if (downS < MAX_DOWNLOADS) {
            int getsize = MAX_DOWNLOADS - downS;
            String[] vas = null;
            if (downS > 0) {
                vas = new String[downS];
                for (int i = 0; i < downS; i++) {
                    vas[i] = getDownloads().get(i).getModel().getB6path();
                }
            }
            List<DownloadModel> models = Operate.getInstance(context).getDownloadLimitModels(getsize, vas);
            APPLog.e(TAG_MD + "-initDownload-models: size=" + models.size(), models);
            for (DownloadModel model : models) {
                DownloadB downloadB = new DownloadUtil(this, model);
                getDownloads().add(downloadB);
                downloadB.start();
            }
        }
    }

    /**
     * 是否可以下载
     *
     * @return
     */
    private boolean canDownload(boolean ishitn) {
        //手写网络可以下载
        if (FileUtils.getInstance().getUseMenoryLong() < 1024 * 1024 * 20) {
            if (ishitn)
                setAlertDialog("内存不足，请删除不必要的文件");
        } else {
            if (ismobilDownload) {
                if (NetWorkUtil.isNetworkConnected(context)) {
                    return true;
                } else if (ishitn) {
                    //提示网络误网络连接
                    setAlertDialog("请检查网络连接！！");
                }
            } else {
                if (NetWorkUtil.isWifiConnected(context)) {
                    return true;
                } else if (ishitn) {
                    //提示连接wifi
                    setAlertDialog("请检查wifi连接！！");
                }
            }
        }
        return false;
    }

    private String getErorMsg() {
        String msg = "下载出小差啦！！";
        if (ismobilDownload) {
            if (!NetWorkUtil.isNetworkConnected(context)) {
                //提示网络误网络连接
                msg = "请检查网络连接！！";
            }
        } else if (!NetWorkUtil.isWifiConnected(context)) {
            //提示连接wifi
            msg = "请检查wifi连接！！";
        } else if (FileUtils.getInstance().getUseMenoryLong() < 1024 * 1024 * 20) {
            msg = "内存不足，请删除不必要的文件，重新下载";
        }
        return msg;
    }

    private void setAlertDialog(String value) {
        if (alertDialog == null || !alertDialog.isshow()) {
            alertDialog = null;
            alertDialog = new AlertDialog(context,0)
                    .setTitle("网络提示")
                    .setMsg(value);
            alertDialog.show();
        } else {
            alertDialog.setMsg(value);
        }
    }

    public synchronized Set<EventInterface> getEvents() {
        return events;
    }

    public synchronized List<DownloadB> getDownloads() {
        return downloads;
    }

    /**
     * 注册通知事件
     *
     * @param event 注册事件
     */
    @Override
    public void registerEvent(@NonNull EventInterface event) {
        getEvents().add(event);
    }

    /**
     * 解注册事件
     *
     * @param event 注册事件
     */
    @Override
    public void unRegisterEvent(@NonNull EventInterface event) {
        getEvents().remove(event);
    }

    private synchronized void removedownloadByPath(String path) {
        DownloadB dbv = null;
        for (DownloadB d : downloads) {
            if (d.getModel().getPath().equals(path)) {
                dbv = d;
                break;
            }
        }
        if (dbv != null) {
            getDownloads().remove(dbv);
        }
    }

    @Override
    public void onSuccess(final String downloadPath) {
        APPLog.e(TAG_MD, "onSuccess:" + downloadPath);
        Operate.getInstance(context).updateStatus(downloadPath, Config.download_success);
        for (final EventInterface event : events) {
            if (event.isAcceptDownload()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        event.onSuccess(downloadPath);
                    }
                });
            }
        }
        removedownloadByPath(downloadPath);
        initDownload();
    }

    @Override
    public void onDownloadStart(final String downloadPath) {
        APPLog.e(TAG_MD, "onDownloadStart:" + downloadPath);
        Operate.getInstance(context).updateStatus(downloadPath, Config.download_loding);
        for (final EventInterface event : events) {
            if (event.isAcceptDownload()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        event.onDownloadStart(downloadPath);
                    }
                });
            }
        }
    }

    @Override
    public void onDownloading(final String downloadPath, final int curDownlaod, final long count, final long currentsize) {
        APPLog.e(TAG_MD, "onDownloading:" + downloadPath + "  curDownlaod=" + curDownlaod + "  count=" + count + "  currentsize=" + currentsize);
        Operate.getInstance(context).updateProgress(downloadPath, count, currentsize);
        for (final EventInterface event : events) {
            if (event.isAcceptDownload()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        event.updateDownload(downloadPath, curDownlaod, count, currentsize);
                    }
                });
            }
        }
    }

    @Override
    public void onFailed(final String downloadPath) {
        APPLog.e(TAG_MD, "onFailed:" + downloadPath);
        if (canDownload(false)) {//网络，内存正常情况下删除下载备注下载失败
            Operate.getInstance(context).updateStatus(downloadPath, Config.download_faile);
            removedownloadByPath(downloadPath);
            initDownload();
        }
        for (final EventInterface event : events) {
            if (event.isAcceptDownload()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        event.onFailed(downloadPath, getErorMsg());
                    }
                });
            }
        }
    }

    @Override
    public void onPasue(final String downloadPath) {
        APPLog.e(TAG_MD, "onPasue:" + downloadPath);
        Operate.getInstance(context).updateStatus(downloadPath, Config.download_pause);
        for (final EventInterface event : events) {
            if (event.isAcceptDownload()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        event.onPasue(downloadPath);
                    }
                });
            }
        }
        removedownloadByPath(downloadPath);
        initDownload();
    }

    @Override
    public void onDelete(String downloadPath) {
        APPLog.e(TAG_MD, "onDelete:" + downloadPath);
        StringUtils.deleteFile(Utils.getSavePath(downloadPath));
        Operate.getInstance(context).delete(false, downloadPath);
        removedownloadByPath(downloadPath);
        initDownload();
    }
}
