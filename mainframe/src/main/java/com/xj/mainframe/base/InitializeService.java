package com.xj.mainframe.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tencent.smtt.sdk.QbSdk;
import com.xj.mainframe.configer.APPLog;

/**
 * Created by xj on 2018/11/15.
 */

public abstract class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.anly.githubapp.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static<T> void start(@NonNull Context context, Class<T> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        APPLog.d("performInit begin:" + System.currentTimeMillis());
        //X5内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                APPLog.e("BaseApplication-app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        performInitService();

        APPLog.d("performInit end:" + System.currentTimeMillis());
    }

    /**
     * 控件初始化
     */
   protected abstract void performInitService();
}
