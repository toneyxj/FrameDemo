package com.xj.mainframe.listener;

import android.view.View;

import com.xj.mainframe.configer.APPLog;

/**
 * Created by xj on 2018/10/25.
 */

public abstract class XJOnClickListener implements View.OnClickListener {
    /**
     * 事件控制避免反复点击
     */
    private static long time = 0;
    @Override
    public void onClick(View v) {
        if (time != 0 && (System.currentTimeMillis() - time) < 500) {
            APPLog.d("onClick","点击过频繁");
            return;
        }
        time = System.currentTimeMillis();
        onclickView(v);
    }
    /**
     * view点击
     * @param view
     */
    public abstract void onclickView(View view);
}
