package com.xj.mainframe.netState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.xj.mainframe.configer.APPLog;

import java.util.ArrayList;

/**
 * 网络状态监听管理
 * Created by xj on 2018/9/14.
 */
public class NetWorkStateUtil {
    private static NetWorkStateUtil instatnce = null;

    public static NetWorkStateUtil getInstance(Context context) {
        if (instatnce == null) {
            synchronized (NetWorkStateUtil.class) {
                if (instatnce == null) {
                    instatnce = new NetWorkStateUtil(context.getApplicationContext());
                }
            }
        }
        return instatnce;
    }
    private final static String ANDROID_NET_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;//"android.net.conn.CONNECTIVITY_CHANGE";
    private  Boolean networkAvailable = false;
    private  NetWorkUtil.netType netType;
    private ArrayList<NetChangeObserver> netObsers=new ArrayList<>();     //存放观察者引用的数组线性表
    private Context context;
    private long registerTime;

    public  NetWorkStateUtil(Context context ) {
        registerTime=System.currentTimeMillis();
        this.context=context.getApplicationContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        this.context.registerReceiver(receiver, filter);
    }

    /**
     * 清空监听
     */
    public void clearObsers(){
        netObsers.clear();
        unregisterNetReceiver();
        instatnce=null;
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
                if (!NetWorkUtil.isNetworkConnected(context)) {
                    networkAvailable = false;
                } else {
                    netType = NetWorkUtil.getAPNType(context);
                    networkAvailable = true;
                }
                notifyObserver();
            }
        }
    };

    public void registerObserver(NetChangeObserver observer) {
            if (!netObsers.contains(observer))netObsers.add(observer);
    }

    public void removeObserver(NetChangeObserver observer) {
        netObsers.remove(observer);
    }

    public void notifyObserver() {
        long tt=System.currentTimeMillis();
        if (Math.abs(tt-registerTime)<=500) {
            APPLog.d("notifyObserver","first register delayed 500 millisecond");
            return;
        }
        int size=netObsers.size();
        for (int i = 0; i <size ; i++) {
            if (networkAvailable){
                netObsers.get(i).onConnect(netType);
            }else {
                netObsers.get(i).onDisConnect();
            }

        }
    }
    /**
     * 注销网络状态广播
     */
    public  void unregisterNetReceiver() {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
