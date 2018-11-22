package com.xj.framedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xj.mainframe.configer.APPLog;
import com.xj.mainframe.configer.ToastUtils;
import com.xj.mainframe.dialog.AlertDialog;
import com.xj.mainframe.download.Dinterface.DMBase;
import com.xj.mainframe.eventBus.EventManger;
import com.xj.mainframe.eventBus.EventObserver;
import com.xj.mainframe.listener.AlertInterface;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.netState.NetChangeObserver;
import com.xj.mainframe.netState.NetWorkStateUtil;
import com.xj.mainframe.netState.NetWorkUtil;
import com.xj.mainframe.utils.SystemUtils;
import com.xj.mainframe.view.BaseView.XJImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
    ArrayList<String> urls=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urls.add("http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h178_1.41.2.16_5a7ef8.apk");
        urls.add("http://gdownyf.baijincdn.cn/data/wisegame/d3cfcbced1a50905/meituan_697.apk");
        urls.add("http://gdownyf.baijincdn.cn/data/wisegame/7755dede9585bf18/yingyongbao_7292130.apk");
        urls.add("http://gdownyf.baijincdn.cn/data/wisegame/6b9bb3afc505fdee/qichezhijia_965.apk");
        APPLog.e("SystemUtils.getCPUCoreNum()", SystemUtils.getCPUCoreNum());
        for (String url:urls) {
            DMBase.getInstance(this).addDownload(url);
        }
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
                new AlertDialog(MainActivity.this).builder(0)
                        .setTitle("网络提示")
                        .setMsg("提示").show();
//                startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
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
        Set<String> sets= new HashSet<>();
        sets.add("就是这么");
        sets.add("就是这么");
        sets.add("就是这么");
        sets.add("就是这么1");
        APPLog.e("sets",sets.size());

        NetWorkStateUtil.getInstance(this).registerObserver(this);

        EventManger.getInstance().registerObserver(mainE,this);

        String  value="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1540793816775&di" +
                "=320911c5448aba2c236006c27e8d4024&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Ftj%2F2018-09-27%2F5baca04abc904.jpg";

//        String has= Base64Util.encodeData(value);
//        APPLog.e("转换",has );
//        APPLog.e("转换",Base64Util.decodeData(value) );
//        thread.start();

//        findViewById(R.id.one).setOnClickListener(clickListener);
//        findViewById(R.id.two).setOnClickListener(clickListener);
//        findViewById(R.id.three).setOnClickListener(clickListener);
//        findViewById(R.id.four).setOnClickListener(clickListener);
    }

    private AlertInterface anInterface=new AlertInterface() {
        @Override
        public boolean onNegative(int code) {
            ToastUtils.getInstance().showToastShort("点击onNegative code="+code);
            return true;
        }

        @Override
        public boolean onPositive(int code) {
            ToastUtils.getInstance().showToastShort("点击onPositive code="+code);
            return true;
        }
    };
private XJOnClickListener clickListener=new XJOnClickListener() {
    @Override
    public void onclickView(View view) {
//        switch (view.getId()){
//            case R.id.one:
//                DMBase.getInstance(MainActivity.this).switchDownload(urls.get(0));
//                break;
//            case R.id.two:
//                DMBase.getInstance(MainActivity.this).switchDownload(urls.get(1));
//                break;
//            case R.id.three:
//                DMBase.getInstance(MainActivity.this).switchDownload(urls.get(2));
//                break;
//            case R.id.four:
//                DMBase.getInstance(MainActivity.this).switchDownload(urls.get(3));
//                break;
//            default:
//                break;
//        }
    }
};
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
//    private Thread thread=new Thread(){
//        @Override
//        public void run() {
//            super.run();
//            Log.e("thread","最开始的哟");
//            int index=0;
//            Looper.prepare();
//            Handler handler=new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    Log.e("thread","handleMessage="+msg.what);
//                }
//            };
//
//            Log.e("thread","prepare-开始  handler="+handler.toString());
//            try {
//                sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            handler.sendEmptyMessage(index);
//            index++;
//            Looper.loop();
//            Log.e("thread","prepare-结束");
//        }
//    };
}
