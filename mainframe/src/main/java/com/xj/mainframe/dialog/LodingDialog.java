package com.xj.mainframe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xj.mainframe.R;
import com.xj.mainframe.utils.DensityUtil;

/**
 * Created by xj on 2018/11/7.
 */

public class LodingDialog extends Dialog {
    public LodingDialog(Context context) {
        super(context, R.style.request_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout=new FrameLayout(getContext());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);
        frameLayout.setBackgroundColor(getContext().getResources().getColor(R.color.transparency));
        int px= DensityUtil.dip2px(getContext(),16);
        frameLayout.setPadding(px,px,px,px);

        this.setContentView(frameLayout);

        ImageView imageView=new ImageView(getContext());
        imageView.setLayoutParams(params);
        imageView.setImageResource(R.drawable.loding_logo);

        //动画
        AnimationSet animationSet=new AnimationSet(true);
        RotateAnimation animation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Integer.MAX_VALUE);
        animation.setRepeatMode(Animation.RESTART);
        animationSet.addAnimation(animation);

        imageView.startAnimation(animation);

        frameLayout.addView(imageView);
    }
    public static Dialog getDialog(Context context){
        LodingDialog dialog=new LodingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        return dialog;
    }
}
