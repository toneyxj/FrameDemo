package com.xj.mainframe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xj.mainframe.R;
import com.xj.mainframe.listener.AlertInterface;
import com.xj.mainframe.listener.XJOnClickListener;
import com.xj.mainframe.utils.ActivityUtils;
import com.xj.mainframe.utils.DensityUtil;
import com.xj.mainframe.view.BaseView.XJImageView;
import com.xj.mainframe.view.extendView.AlphaButton;


public class AlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private XJImageView img_msg;
    private AlphaButton btn_neg;
    private AlphaButton btn_pos;
    private View img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showImg = false;
    private AlertInterface alertInterface;
    private int code;

    public AlertDialog(Context context,int code) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        builder(code);

    }

    /**
     * 创建一个dialog
     * @param code 唯一标识
     * @return 返回一个创建的提示框
     */
    private AlertDialog builder(int code) {
        this.code=code;
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_alert, null);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        img_msg = (XJImageView) view.findViewById(R.id.img_dialog);
        img_msg.setVisibility(View.GONE);
        btn_neg = (AlphaButton) view.findViewById(R.id.btn_neg);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (AlphaButton) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);

        //设置背景大小
        Window window = dialog.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        window.setWindowAnimations(R.style.enlargement_animation);
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(DensityUtil.getScreenW(context)/ 8, 0, DensityUtil.getScreenW(context)/ 8, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        return this;
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    /**
     * 设置点击的回调接口
     * @param alertInterface
     * @return
     */
    public AlertDialog setAlertInterface(AlertInterface alertInterface) {
        this.alertInterface = alertInterface;
        return this;
    }

    public AlertDialog setImg(String url) {
        showImg = true;
        img_msg.loadImage(url);
        return this;
    }

    public AlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }
    public void setMsg(SpannableStringBuilder builder){
            txt_msg.setText(builder);
    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(String text) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(clickListener);
        return this;
    }

    public AlertDialog setNegativeButton(String text) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(clickListener);
        return this;
    }
    private XJOnClickListener clickListener=new XJOnClickListener() {
        @Override
        public void onclickView(View view) {
            if (alertInterface==null){
                hide();
            }else if(view.getId()==R.id.btn_neg) {
                if (alertInterface.onNegative(code))hide();
            }else if(view.getId()==R.id.btn_pos) {
                if(alertInterface.onPositive(code))hide();
            }
        }
    };

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (showImg) {
            img_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setOnClickListener(clickListener);
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_neg.setVisibility(View.VISIBLE);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
        }
    }
    public boolean isshow(){
        return dialog!=null&&dialog.isShowing();
    }

    public void show() {
        try {
            if (ActivityUtils.isContextExisted(context)) {
                setLayout();
                dialog.show();
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
    public void hide(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.cancel();
        }
    }
}
