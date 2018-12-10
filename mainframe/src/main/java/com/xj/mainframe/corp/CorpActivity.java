package com.xj.mainframe.corp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xj.mainframe.R;
import com.xj.mainframe.eventBus.EventManger;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.utils.DensityUtil;
import com.xj.mainframe.utils.StatusBarUtil;
import com.xj.mainframe.view.extendView.AlphaImageView;

/**
 * 图片剪切处理
 * Created by xj on 2018/12/7.
 */
public class CorpActivity extends Activity {
    /**
     * 标记返回的code
     */
    public static final int CORP_CODE=100001;

    public static void startCorpActivity(Context context, @NonNull CorpModel model, @NonNull String pathimg){
        Intent intent=new Intent(context,CorpActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",model);
        bundle.putString("pathimg",pathimg);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    private CorpLayout corpLayout;
    private AlphaImageView clickback;
    private AlphaImageView clickInsure;

    private CorpModel model;
    private String pathimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            savedInstanceState=getIntent().getExtras();
        }
        model= (CorpModel) savedInstanceState.getSerializable("data");
        pathimg=savedInstanceState.getString("pathimg");

        StatusBarUtil.darkMode(this);
        //图片剪切布局处理
        corpLayout=new CorpLayout(this);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        corpLayout.setLayoutParams(params);
        setContentView(corpLayout);

        corpLayout.setCorpValue(model,pathimg);

        //添加点击按钮
        Bitmap bitmapback= BitmapFactory.decodeResource(getResources(),R.drawable.loding_logo);
        Bitmap bitmapinsure= BitmapFactory.decodeResource(getResources(),R.drawable.loding_logo);

        int width= DensityUtil.getScreenW(this);
        int height= DensityUtil.getScreenH(this);
        int backy=bitmapback.getHeight();
        int insurex=bitmapinsure.getWidth();

        FrameLayout.LayoutParams params2=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        clickback=new AlphaImageView(this);
        clickback.setImageBitmap(bitmapback);

        int backtop=height-backy-DensityUtil.px2dip(this,16);
        int backLeft=DensityUtil.px2dip(this,16);
        params2.setMargins(backLeft,backtop,0,0);
        clickback.setLayoutParams(params2);

        clickInsure=new AlphaImageView(this);
        clickInsure.setImageBitmap(bitmapinsure);

        FrameLayout.LayoutParams params3=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int insureLeft=width-DensityUtil.px2dip(this,16)-insurex;
        params3.setMargins(insureLeft,backtop,0,0);
        clickInsure.setLayoutParams(params3);

        clickback.setOnClickListener(listener);
        clickInsure.setOnClickListener(listener);

        corpLayout.addView(clickback);
        corpLayout.addView(clickInsure);
    }
    private boolean is=false;

    private XJOnClickListener listener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            if (view==clickback){
                onBackPressed();
            }else {//获得图片保存路径
                if (is)return;
                is=true;
                String value=corpLayout.getCorpBitmap();
                EventManger.getInstance().notifiyCode(CORP_CODE,value);
                onBackPressed();
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data",model);
        outState.putString("pathimg",pathimg);
    }
}
