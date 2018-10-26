package com.xj.mainframe.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;

import com.xj.mainframe.base.BaseUtils;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.listener.BaseActivityInterface;
import com.xj.mainframe.listener.HandlerMessageInterface;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.utils.SoftKeyboardUtils;
import com.xj.mainframe.view.listener.TitleInterface;

/**
 * activity基本控制类
 * Created by xj on 2018/10/25.
 */
public abstract class BaseActivity extends FragmentActivity implements BaseActivityInterface,HandlerMessageInterface
,TitleInterface{
    /**
     * fragment控制器
     */
    private FragmentManager FM;
    private BaseUtils.XJHander handler = new BaseUtils.XJHander(this);
    /**
     * 点击事件处理
     */
    public XJOnClickListener clickListener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            BaseActivity.this.onclickView(view);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        FM = getSupportFragmentManager();
        initActivity(savedInstanceState);
    }

    public BaseUtils.XJHander getHandler() {
        return handler;
    }

    public FragmentManager getFM() {
        return FM;
    }

    /**
     * 显示提示
     * @param value
     */
    public void showToast(Object value) {
        if(!isFinishing()) {
            ToastUtils.getInstance().showToastShort(value);
        }
    }
    /**
     * 点击其它地方关闭软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (SoftKeyboardUtils.isShouldHideInput(v, ev)) {
                SoftKeyboardUtils.closeIMM(this, v.getWindowToken());
                v.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public int getLayoutStatus() {
        return 0;
    }

    //titleListener配置中


    @Override
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onClickTitle() {

    }

    @Override
    public void onClickRTxt() {

    }

    @Override
    public void onClickImg() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getHandler().removeCallbacksAndMessages(null);
    }
}
