package com.xj.mainframe.request.Listener;

import com.alibaba.fastjson.JSONObject;
import com.xj.mainframe.request.RequestModel;

/**
 * 请求数据监听，观察者
 * Created by xj on 2018/10/29.
 */
public interface RequestObserver {
    void onSucess(RequestModel code, JSONObject json);
    void onFaile(RequestModel code,int status ,String msg);
    void onstart(RequestModel code);
}
