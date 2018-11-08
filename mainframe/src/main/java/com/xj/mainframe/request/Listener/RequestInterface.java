package com.xj.mainframe.request.Listener;

import android.content.Context;

import com.xj.mainframe.view.listener.LoadingInterface;

/**
 * 实体数据请求处理需要基础的类
 * Created by xj on 2018/11/7.
 */
public interface RequestInterface {
    /**
     * 显示提示语，普通的请求提醒
     */
    void showToast();

    /**
     * 获得loadingView
     */
    LoadingInterface getLoadingInterface();
    /**
     * 隐藏提示
     */
    void hideShow();

    /**
     * 获得当前控件的view
     * @return
     */
    Context getContext();

    /**
     * 终止数据请求一般发生在请求结束
     */
    void onStopRequest();

}
