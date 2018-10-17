package com.xj.mainframe.view.listener;

import android.graphics.Canvas;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xj on 2018/9/14.
 */

public interface ViewAlphaInterface  {
    /**
     * 界面布局改变调用
     * @param changed
     */
    void onLayout(boolean changed);

    /**
     * 点击监听事件
     * @param event 事件
     * @param back 事件是否已经指定拦截
     */
    void onTouchEvent(MotionEvent event,boolean back);

    /**
     * 获得布局总可改变view
     * @param view
     * @return
     */
    List<TextView> getAllChildViews(View view);

    /**
     * 改变布局颜色
     * @param down
     */
    void changeTextColor(boolean down);

    /**
     * 关闭颜色透明度调节
     * @param Alpha
     */
    void setCloseAlpha(boolean Alpha);

    /**
     * 绘制调用
     * @param canvas
     */
    void onDraw(Canvas canvas);

    /**
     * 界面布局销毁调用
     */
    void onDestory();

    /**
     * 更新布局颜色
     */
    void initViewColor();

    /**
     * handler数据通知
     * @param msg
     */
    void handleMessage(Message msg);
}
