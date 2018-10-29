package com.xj.mainframe.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 数据请求基本返回数据类型
 * Created by xj on 2018/10/29.
 */
public class ResponseModel {
    private int status;
    private String message;
    private JSONObject data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    /**
     * 无网络数据结构
     */
    public static ResponseModel  initNoNet(){
        ResponseModel model=new ResponseModel();
        model.status=-1;
        model.setMessage("请检查网络连接");
        return model;
    }
}
