package com.xj.framedemo;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.request.BaseRequest;
import com.xj.mainframe.request.RequestModel;
import com.xj.mainframe.view.listener.LoadingInterface;

/**
 * Created by xj on 2018/11/7.
 */

public class TestRequest extends BaseRequest {
    public TestRequest(Context context, LoadingInterface loding) {
        super(context, loding);
    }

    @Override
    public boolean resultDataBackIsEmpty(RequestModel code, JSONObject json) {
        APPLog.e("code.getCode()", code.getCode());
        return false;
    }

    public void testRequest() {
        String url = "http://e.dangdang.com/media/api2.go";
        new RequestModel(url, "test", this)
                .addParameter("action", "mediaCategory")
                .addParameter("channelType", "dddsonly")
                .addParameter("start", "0")
                .addParameter("level", "4")
                .addParameter("end", "5")
                .isGet()
                .setCanCache(true)
                .setShowLoidingLayout(false)
                .Builder();
    }

}
