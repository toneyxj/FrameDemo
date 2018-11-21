package com.xj.mainframe.request;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.dialog.LodingDialog;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.request.Listener.RequestInterface;
import com.xj.mainframe.request.Listener.RequestObserver;
import com.xj.mainframe.view.listener.LoadingInterface;

/**
 * 数据请求基本够着类
 * Created by xj on 2018/11/7.
 */

public abstract class BaseRequest implements RequestInterface, RequestObserver {
    private Context context;
    private boolean isFinish = false;
    private LoadingInterface loding;
    private Dialog dialog;
    private RequestModel model;

    public BaseRequest(@NonNull Context context, LoadingInterface loding) {
        isFinish = false;
        this.context = context;
        this.loding = loding;
        if (getLoadingInterface() != null) getLoadingInterface().setListener(clickListener);
        RequestManager.getInstance().registerObserver(this);
    }

    public void setLoding(LoadingInterface loding) {
        this.loding = loding;
    }

    @Override
    public Context getContext() {
        return context;
    }

    private XJOnClickListener clickListener = new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            if (model != null) {
                model.Builder();
            }
        }
    };

    @Override
    public void showToast() {
        if (isFinish) return;
        if (getLoadingInterface() != null) {
            getLoadingInterface().showLoading();
        } else if (context != null) {
            if (dialog == null || !dialog.isShowing()) {
                dialog = LodingDialog.getDialog(context);
            }
        }

    }

    @Override
    public LoadingInterface getLoadingInterface() {
        return loding;
    }

    @Override
    public void hideShow() {
        if (isFinish) return;
        if (loding != null) {
            loding.hideLayout();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }

    }

    @Override
    public void onStopRequest() {
        isFinish = true;
        RequestManager.getInstance().removeObserver(this);
        loding = null;
        hideShow();
        context = null;
    }

    @Override
    public void onFaile(RequestModel code, int status, String msg) {
        if (isFinish) return;
        APPLog.e("status:"+status+"  msg:"+msg);
        try {
            hideShow();
            //不显示任何提示
            if (!code.isShowFailHide()) return;
            if (code.isShowLoidingLayout() && getLoadingInterface() != null) {
                getLoadingInterface().showError(msg, "");
            } else {
                ToastUtils.getInstance().showToastShort(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            hideShow();
            ToastUtils.getInstance().showToastShort(e.getMessage());
        }
    }

    @Override
    public void onstart(RequestModel code) {
        if (isFinish) return;
        model = code;
        if (code.isShowFailHide()) {
            showToast();
        }
    }

    @Override
    public void onSucess(RequestModel code, JSONObject json) {
        try {
            if (isFinish) return;
            hideShow();
            boolean eb = resultDataBackIsEmpty(code, json);
            if (eb && code.isShowLoidingLayout() && getLoadingInterface() != null) {
                //空数据显示布局
                getLoadingInterface().showEmpty("暂无数据");
            } else {
                if (model == code) model = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLoadingInterface().showError(e.getMessage(), "");
        }
    }

    /**
     * 数据请求结果
     *
     * @param json 请求数据data
     * @return 返回数据解析结果是否为空，true为空，false为数据解析没有问题
     */
    public abstract boolean resultDataBackIsEmpty(RequestModel code, JSONObject json);
}