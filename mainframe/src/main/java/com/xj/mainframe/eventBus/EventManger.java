package com.xj.mainframe.eventBus;

import android.os.Handler;

import com.xj.mainframe.configer.APPLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj on 2018/11/8.
 */

public class EventManger {
    // 初始化类实列
    private static EventManger instatnce = null;
    private Handler handler=new Handler();

    /**
     * 获得软键盘弹出类实列
     *
     * @return 返回初始化实列
     */
    public static EventManger getInstance() {
        if (instatnce == null) {
            synchronized (EventManger.class) {
                if (instatnce == null) {
                    instatnce = new EventManger();
                }
            }
        }
        return instatnce;
    }


    public Map<Integer,EventObserver> events=new HashMap<>();

    public synchronized void registerObserver(Integer code,EventObserver observer) {
        if (code<0){
//            throw new RuntimeException("code has to be greater than zero");
            APPLog.e("code has to be greater than zero");
            return;
        }else if (observer==null){
//            throw new NullPointerException("observer not null");
            APPLog.e("observer not null");
            return;
        }
        events.put(code,observer);
    }

    public synchronized void removeObserver(int code) {
        events.remove(code);
    }

    /**
     * 通知所有注册的事件
     */
    public synchronized void notifiyAll(Object obj){
        for (Map.Entry<Integer,EventObserver> entry:events.entrySet()){
            entry.getValue().eventUpdate(entry.getKey(),obj);
        }
    }

    /**
     * 通知指定的注册code事件
     * @param code
     * @param obj
     */
    public synchronized void notifiyCode(final int code, final Object obj){
        handler.post(new Runnable() {
            @Override
            public void run() {
                EventObserver observer= events.get(code);
                if (observer!=null){
                    observer.eventUpdate(code,obj);
                }
            }
        });
    }


}
