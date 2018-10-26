package com.xj.mainframe.listener;

import android.os.Bundle;
import android.view.View;

/**
 * Created by xj on 2018/10/25.
 */

public interface BaseActivityInterface {

    /**
     * 执行activity.oncreate,数据初始化
     * @param savedInstanceState
     */
    void initActivity(Bundle savedInstanceState);
    /**
     *初始化view控件initActivity后执行
     */
    void initView();
    /**
     * 获得布局的id
     * @return
     */
    int getLayoutID();

    /**
     * 获得布局状态0默认布局，1使用当前赋值的布局
     * @return
     */
    int getLayoutStatus();
    /**
     * view点击
     * @param view
     */
    void onclickView(View view);
}
