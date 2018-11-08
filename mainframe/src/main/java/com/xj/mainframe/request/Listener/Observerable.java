package com.xj.mainframe.request.Listener;

/**
 * 观察者模式基本类型
 * Created by xj on 2018/10/29.
 */
public interface Observerable {
    public void registerObserver(RequestObserver observer);
    public void removeObserver(RequestObserver observer);
}
