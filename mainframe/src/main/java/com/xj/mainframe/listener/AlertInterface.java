package com.xj.mainframe.listener;

/**
 * Created by xj on 2018/11/21.
 */

public interface AlertInterface {
    /**
     * AlertDialog，设置的Negative点击监听
     * @param code 方法唯一标识
     *             @return 返回true：点击按钮后提示框消失
     */
    boolean onNegative(int code);

    /**
     * AlertDialog，设置的Positive点击监听
     * @param code 方法唯一标识
     */
    boolean onPositive(int code);
}
