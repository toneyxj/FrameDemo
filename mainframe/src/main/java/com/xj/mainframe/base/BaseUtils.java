package com.xj.mainframe.base;

import android.os.Handler;
import android.os.Message;

import com.xj.mainframe.listener.HandlerMessageInterface;

import java.lang.ref.WeakReference;

/**
 * base操作工具类
 */

public class BaseUtils {

    /**
     * 全部类使用handler
     */
    public static class XJHander extends Handler {
        private WeakReference<HandlerMessageInterface> reference;

        public XJHander(HandlerMessageInterface context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerMessageInterface activity = (HandlerMessageInterface) reference.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
