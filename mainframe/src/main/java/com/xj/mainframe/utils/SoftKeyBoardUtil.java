package com.xj.mainframe.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.xj.mainframe.listener.OnSoftKeyBoardChangeListener;

/**
 * 获取界面根视图高度变化情况
 * Created by xj on 2018/9/14.
 */
public class SoftKeyBoardUtil implements ViewTreeObserver.OnGlobalLayoutListener {
    private View rootView;//activity的根视图
    private OnSoftKeyBoardChangeListener listener;
    private int rootViewVisibleHeight;//纪录根视图的显示高度

    public SoftKeyBoardUtil(Activity activity, OnSoftKeyBoardChangeListener listener) {
        this.listener = listener;
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();
    }

    @Override
    public void onGlobalLayout() {
        //获取当前根视图在屏幕上显示的大小
        Rect r = new Rect();
        //计算获得没有状态栏的屏幕显示高度
        rootView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return;
        }

        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewVisibleHeight - visibleHeight > 200) {
            if (listener != null) {
                listener.keyBoardShow(rootViewVisibleHeight - visibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }

        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewVisibleHeight > 200) {
            if (listener != null) {
                listener.keyBoardHide(visibleHeight - rootViewVisibleHeight);
            }
            rootViewVisibleHeight = visibleHeight;
            return;
        }
    }

    /**
     * 销毁添加监听
     */
    public void onDestory() {
        if (rootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    }
}
