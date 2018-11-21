package com.xj.framedemo;

import android.os.Debug;

import com.xj.mainframe.BaseApplication;
import com.xj.mainframe.base.InitializeService;

/**
 * Created by xj on 2018/10/23.
 */

public class ObjApp extends BaseApplication {
    @Override
    public void onCreate() {
        Debug.startMethodTracing("ObjApp");
        super.onCreate();
//注册提示
        InitializeService.start(this,myInitService.class);
        Debug.stopMethodTracing();
    }
}
