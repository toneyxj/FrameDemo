package com.xj.framedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xj.framedemo.testrefuresh.ScrollingActivity;
import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.eventBus.EventManger;
import com.xj.mainframe.eventBus.EventObserver;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.netState.NetChangeObserver;
import com.xj.mainframe.netState.NetWorkStateUtil;
import com.xj.mainframe.netState.NetWorkUtil;
import com.xj.mainframe.view.BaseView.XJImageView;

import java.util.LinkedList;

public class MainActivity extends Activity implements NetChangeObserver,EventObserver {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
//    @Bind(R.id.buttlay)
//    LinearLayout buttlay;
//    @Bind(R.id.sample_text)
//    XJTextView sample_text;

    public static int mainE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

//        buttlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ((Button)findViewById(R.id.click)).setTextColor(getResources().getColor(R.color.colorAccent));
//            }
//        });
        (findViewById(R.id.click)).setOnClickListener(new XJOnClickListener() {
            @Override
            public void onclickView(View view) {
                startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
//                BrowserActivity.StartBrowser(MainActivity.this,"http://soft.imtt.qq.com/browser/tes/feedback.html",false);
            }
        });

        XJImageView imag=(XJImageView)findViewById(R.id.imag);
        imag.loadImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540793816775&di=320911c5448aba2c236006c27e8d4024&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Ftj%2F2018-09-27%2F5baca04abc904.jpg");

         LinkedList<String> observers=new LinkedList<>();
        observers.add("就是这么");
        observers.add("就是这么");
        observers.add("就是这么");
        observers.add("就是这么");
        APPLog.e("observers",observers.size());
        NetWorkStateUtil.getInstance(this).registerObserver(this);

        EventManger.getInstance().registerObserver(mainE,this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkStateUtil.getInstance(this).removeObserver(this);
//        ButterKnife.unbind(this);
        EventManger.getInstance().removeObserver(mainE);
    }

    @Override
    public void onConnect(NetWorkUtil.netType type) {
        APPLog.d("网络连接了");
    }

    @Override
    public void onDisConnect() {
        APPLog.d("网络连接关闭了");
    }

    @Override
    public void eventUpdate(int code, Object data) {
        if (code==mainE){
            ToastUtils.getInstance().showToastShort(data.toString());
        }
    }
}
