package com.xj.mainframe;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
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
        ToastUtils.getInstance().initToast(getApplicationContext());
        context=getApplicationContext();
        refWatcher = LeakCanary.install(this);
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
