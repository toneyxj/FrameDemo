package com.xj.mainframe.view.listener;

import android.view.View;

/**
 * Created by xj on 2018/11/7.
 */

public interface LoadingInterface {
    /**显示加载进度界面*/
    void showLoading();
    void setListener(View.OnClickListener listener);

    /**展示加载界面数据未空*/
    void showEmpty();
    void showEmpty(String value);

    /**加载错误*/
    void showError();
    void showError(String value,String buttontext);
    /**显示注入的布局提示*/
    void showContent();

    /**隐藏提示*/
    void hideLayout();

}
