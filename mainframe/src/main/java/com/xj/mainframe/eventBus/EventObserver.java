package com.xj.mainframe.eventBus;

/**
 * 事件监听观察者
 * Created by xj on 2018/11/8.
 */
public interface EventObserver {
    void eventUpdate(int code,Object data);
}
