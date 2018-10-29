package com.xj.mainframe;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.utils.DynamicTimeFormat;
import com.xj.refuresh.SmartRefreshLayout;
import com.xj.refuresh.api.DefaultRefreshFooterCreator;
import com.xj.refuresh.api.DefaultRefreshHeaderCreator;
import com.xj.refuresh.api.RefreshFooter;
import com.xj.refuresh.api.RefreshHeader;
import com.xj.refuresh.api.RefreshLayout;
import com.xj.refuresh.footer.ClassicsFooter;
import com.xj.refuresh.header.ClassicsHeader;

/**
 * Created by xj on 2018/9/13.
 */

public class BaseApplication extends Application {
    public static  Context context;
    static {
        //启用矢量图兼容
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setPrimaryColorsId(R.color.default_title_color, android.R.color.white);

                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        context=getApplicationContext();
        refWatcher = LeakCanary.install(this);
        //注册提示
        ToastUtils.getInstance().initToast(getApplicationContext());

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
    }

    /**
     * 在fragment ondestory里面执行
     *
     * @return
     */
    public static void onDestoryFtagment(Fragment fragment) {
        if (refWatcher == null) return;
        refWatcher.watch(fragment);
    }
}
