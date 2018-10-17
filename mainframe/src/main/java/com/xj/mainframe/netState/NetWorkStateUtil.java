package com.xj.mainframe.netState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

/**
 * 网络状态监听管理
 * Created by xj on 2018/9/14.
 */
public class NetWorkStateUtil {
    private final static String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//"android.net.conn.CONNECTIVITY_CHANGE";
    private  Boolean networkAvailable = false;
    private  NetWorkUtil.netType netType;
    private NetChangeObserver observer;

    public NetWorkStateUtil(Context context,NetChangeObserver observer) {
        this.observer = observer;

        IntentFilter filter = new IntentFilter();
        //filter.addAction(TA_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.registerReceiver(receiver, filter);
    }

    /**
     * 判断是否有网络连接
     * @return
     */
    public Boolean getNetworkAvailable() {
        return networkAvailable;
    }

    /**
     * 判断网络连接状态
     * @return
     */
    public NetWorkUtil.netType getNetType() {
        return netType;
    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
                // TALogger.i(NetworkStateReceiver.this, "网络状态改变.");
                if (!NetWorkUtil.isNetworkAvailable(context)) {
                    // TALogger.i(NetworkStateReceiver.this, "没有网络连接.");
                    networkAvailable = false;
                } else {
                    // TALogger.i(NetworkStateReceiver.this, "网络连接成功.");
                    netType = NetWorkUtil.getAPNType(context);
                    networkAvailable = true;
                }
                if (observer==null)return;
                if (networkAvailable){
                    observer.onConnect(netType);
                }else {
                    observer.onDisConnect();
                }
            }
        }
    };
    /**
     * 注销网络状态广播
     */
    public  void unregisterNetReceiver(Context mContext) {
            try {
                mContext.unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
