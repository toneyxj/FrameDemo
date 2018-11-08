package com.xj.mainframe.base.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xj.mainframe.base.BaseUtils;
import com.xj.mainframe.listener.FragmentInterface;
import com.xj.mainframe.listener.HandlerMessageInterface;
import com.xj.mainframe.listener.XJOnClickListener;

/**
 * Created by xj on 2018/11/7.
 */

public abstract class  BaseFragment extends Fragment implements FragmentInterface,HandlerMessageInterface {

    private BaseUtils.XJHander hander=new BaseUtils.XJHander(this);
    private View rootView;

    public View getRootView() {
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), container);
        initView(rootView);
        return rootView;
    }

    @Override
    public void handleMessage(Message msg) {

    }

    /**
     * 点击事件处理
     */
    public XJOnClickListener clickListener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            BaseFragment.this.onclickView(view);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        hander.removeCallbacksAndMessages(null);
    }
}
